package fs.trinity.daisy.financial_app.transactions.application.service;

import fs.trinity.daisy.financial_app.products.application.service.ProductServicePort;
import fs.trinity.daisy.financial_app.products.domain.models.AccountState;
import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
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

    private final ProductServicePort productServicePort;

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
    public TransactionModel withdrawAmount(Long productOriginId, BigDecimal amount) {
        verifyProductIsActive(productOriginId);
        verifyAmmountIsNotZero(amount);

        return null;
    }

    @Override
    public TransactionModel transferAmount(Long productOriginId, Long productDestinyId, BigDecimal amount) {
        verifyProductIsActive(productOriginId);
        verifyProductIsActive(productDestinyId);
        verifyAmmountIsNotZero(amount);

        return null;
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