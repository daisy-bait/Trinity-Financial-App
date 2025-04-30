package fs.trinity.daisy.financial_app.transactions.domain.ports.input;

import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionModel;

import java.util.List;

public interface TransactionUseCases {

    List<TransactionModel> getAllTransactions();

    TransactionModel getTransactionById(Long transactionId);

    TransactionModel saveTransaction(TransactionModel transactionModel);

    TransactionModel modifyTransaction(TransactionModel transactionModel, Long transactionId);

    boolean deleteTransaction(Long transactionId);

}
