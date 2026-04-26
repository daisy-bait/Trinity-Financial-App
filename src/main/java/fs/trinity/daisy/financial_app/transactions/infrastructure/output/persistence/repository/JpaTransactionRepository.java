package fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.repository;

import fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(value = "SELECT t FROM TransactionEntity t " +
            "WHERE (:productNumber IS NULL OR t.originProduct.productNumber = :productNumber) " +
            "ORDER BY t.transactionDate DESC",
    nativeQuery = false)
    public Page<TransactionEntity> pageByProductNumber(@Param("productNumber") String productNumber, Pageable pageable);

}
