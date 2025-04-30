package fs.trinity.daisy.financial_app.transactions.domain.ports.input;

import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionModel;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionUseCases {

    List<TransactionModel> getAllTransactions();

    TransactionModel getTransactionById(Long transactionId);

    TransactionModel consignAmount(TransactionModel transactionModel);

    TransactionModel withdrawAmount(Long productOriginId, BigDecimal amount);

    TransactionModel transferAmount(Long productOriginId, Long productDestinyId, BigDecimal amount);

    boolean deleteTransaction(Long transactionId);

}
