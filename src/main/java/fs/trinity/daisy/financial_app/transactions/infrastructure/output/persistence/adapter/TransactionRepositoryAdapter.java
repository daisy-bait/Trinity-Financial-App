package fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.adapter;

import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionModel;
import fs.trinity.daisy.financial_app.transactions.domain.ports.output.TransactionRepositoryPort;
import fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.entity.TransactionEntity;
import fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.mapper.TransactionPersistenceMapper;
import fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.repository.JpaTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final JpaTransactionRepository transactionRepo;

    private final TransactionPersistenceMapper mapper;

    @Override
    public List<TransactionModel> findAllTransactions() {
        return transactionRepo.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<TransactionModel> findTransactionById(Long transactionId) {
        return transactionRepo.findById(transactionId).map(mapper::toModel);
    }

    @Override
    public Page<TransactionModel> pageTransactionByProductNumber(Pageable pageable, String productNumber) {
        return transactionRepo.pageByProductNumber(productNumber, pageable).map(mapper::toModel);
    }

    @Override
    public TransactionModel saveTransaction(TransactionModel transaction) {
        TransactionEntity transactionEntity = mapper.toEntity(transaction);
        return mapper.toModel(transactionRepo.save(transactionEntity));
    }

    @Override
    public void deleteTransactionById(Long transactionId) {
        transactionRepo.deleteById(transactionId);
    }

}