package fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.entity;

import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.entity.ProductEntity;
import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "transaction_type")
    private TransactionTypes transactionType;
    private BigDecimal amount;
    @ManyToOne
    private ProductEntity originProduct;
    @ManyToOne
    private ProductEntity destinyProduct;
    @Column(nullable = false, name = "transaction_date")
    private LocalDateTime transactionDate;

}
