package fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.repository;

import fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, Long> {

}
