package fs.trinity.daisy.financial_app.transactions.domain.ports.input;

import fs.trinity.daisy.financial_app.shared.infrastructure.model.PageResponse;
import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionModel;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionUseCases {

    List<TransactionModel> getAllTransactions();

    PageResponse<TransactionModel> pageTransactions(Pageable pageable, String productNumber);

    TransactionModel getTransactionById(Long transactionId);

    TransactionModel consignAmount(TransactionModel transactionModel);

    TransactionModel withdrawAmount(TransactionModel transactionModel);

    TransactionModel transferAmount(TransactionModel transactionModel);

    boolean deleteTransaction(Long transactionId);

}
