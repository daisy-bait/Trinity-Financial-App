package fs.trinity.daisy.financial_app.transactions.domain.ports.output;

import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryPort {

    List<TransactionModel> findAllTransactions();

    Optional<TransactionModel> findTransactionById(Long transactionId);

    Page<TransactionModel> pageTransactionByProductNumber(Pageable pageable, String productNumber);

    TransactionModel saveTransaction(TransactionModel transaction);

    void deleteTransactionById(Long transactionId);

}
