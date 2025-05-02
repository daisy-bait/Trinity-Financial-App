package fs.trinity.daisy.financial_app.transactions.application.service;

import fs.trinity.daisy.financial_app.products.domain.models.AccountState;
import fs.trinity.daisy.financial_app.products.domain.models.AccountTypes;
import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.domain.ports.input.ProductUseCases;
import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionModel;
import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionTypes;
import fs.trinity.daisy.financial_app.transactions.domain.ports.input.TransactionUseCases;
import fs.trinity.daisy.financial_app.transactions.domain.ports.output.TransactionRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class TransactionServicePort implements TransactionUseCases {

    private final TransactionRepositoryPort transactionRepo;

    private final ProductUseCases productServicePort;

    @Override
    public List<TransactionModel> getAllTransactions() {
        return transactionRepo.findAllTransactions();
    }

    @Override
    public TransactionModel getTransactionById(Long transactionId) {
        return transactionRepo.findTransactionById(transactionId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public TransactionModel consignAmount(TransactionModel transactionModel) {
        verifyProductIsActive(transactionModel.getOriginProduct().getId());
        verifyAmmountIsNotZero(transactionModel.getAmount());

        Long productId = transactionModel.getOriginProduct().getId();
        BigDecimal amount = transactionModel.getAmount();

        TransactionModel transaction = new TransactionModel();
        transaction.setTransactionType(TransactionTypes.CONSIGNMENT);
        transaction.setAmount(amount);
        transaction.setOriginProduct(productServicePort.getProduct(productId));
        transaction.setTransactionDate(LocalDateTime.now());

        ProductModel productOrigin = productServicePort.getProduct(productId);
        productOrigin.setBalance(productOrigin.getBalance().add(amount));

        productServicePort.updateProduct(productOrigin);
        return transactionRepo.saveTransaction(transaction);
    }

    @Override
    public TransactionModel withdrawAmount(TransactionModel transactionModel) {

        Long productId = transactionModel.getOriginProduct().getId();
        BigDecimal amount = transactionModel.getAmount();

        verifyProductIsActive(productId);
        verifyAmmountIsNotZero(amount);

        ProductModel productOrigin = productServicePort.getProduct(productId);

        if (!productOrigin.isGmfExempt()) {
            BigDecimal gmf = amount.multiply(BigDecimal.valueOf(0.004));
            amount = amount.add(gmf);
        }

        if (productOrigin.getBalance().compareTo(amount) < 0 && productOrigin.getProductType().equals(AccountTypes.AHORROS)) {
            throw new RuntimeException("The amount is greater than the product balance");
        }

        TransactionModel transaction = new TransactionModel();
        transaction.setTransactionType(TransactionTypes.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setOriginProduct(productOrigin);
        transaction.setTransactionDate(LocalDateTime.now());

        productOrigin.setBalance(productOrigin.getBalance().subtract(amount));

        productServicePort.updateProduct(productOrigin);
        return transactionRepo.saveTransaction(transaction);
    }

    @Override
    public TransactionModel transferAmount(TransactionModel transactionModel) {

        Long originProductId = transactionModel.getOriginProduct().getId();
        Long destinyProductId = transactionModel.getDestinyProduct().getId();
        BigDecimal amount = transactionModel.getAmount();

        if (originProductId == destinyProductId) throw new RuntimeException("Transference between the same Products? What a Dumb");

        verifyProductIsActive(originProductId);
        verifyProductIsActive(destinyProductId);
        verifyAmmountIsNotZero(amount);

        ProductModel productOrigin = productServicePort.getProduct(originProductId);
        ProductModel productDestiny = productServicePort.getProduct(destinyProductId);

        productDestiny.setBalance(productDestiny.getBalance().add(amount));

        if (!productOrigin.isGmfExempt() && productOrigin.getClient().getId() != productDestiny.getClient().getId()) {
            BigDecimal gmf = amount.multiply(BigDecimal.valueOf(0.004));
            amount = amount.add(gmf);
        }

        if (productOrigin.getBalance().compareTo(amount) < 0 && productOrigin.getProductType().equals(AccountTypes.AHORROS)) {
            throw new RuntimeException("The amount is greater than the product origin balance");
        }

        TransactionModel transaction = new TransactionModel();
        transaction.setTransactionType(TransactionTypes.TRANSFERENCE);
        transaction.setAmount(amount);
        transaction.setOriginProduct(productOrigin);
        transaction.setDestinyProduct(productDestiny);
        transaction.setTransactionDate(LocalDateTime.now());

        productOrigin.setBalance(productOrigin.getBalance().subtract(amount));

        productServicePort.updateProduct(productOrigin);
        productServicePort.updateProduct(productDestiny);

        return transactionRepo.saveTransaction(transaction);
    }

    @Override
    public boolean deleteTransaction(Long transactionId) {
        if (transactionRepo.findTransactionById(transactionId).isPresent()) {
            transactionRepo.deleteTransactionById(transactionId);
            return true;
        }
        return false;
    }

    public void verifyProductIsActive(Long productId) {
        ProductModel product = productServicePort.getProduct(productId);
        if (!product.getProductState().equals(AccountState.ACTIVE))
            throw new RuntimeException("Product Account is not active");
    }

    public void verifyAmmountIsNotZero(BigDecimal amount) {
        if (!(amount.compareTo(BigDecimal.ZERO) > 0)) {
            throw new RuntimeException("Amount must be greater than zero");
        }
    }

}