package fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.entity;

import fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.entity.ProductEntity;
import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

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
    @JoinColumn(name = "origin_product_id", foreignKey = @ForeignKey(name = "fk_origin_product"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductEntity originProduct;
    @ManyToOne
    @JoinColumn(name = "destiny_product_id", foreignKey = @ForeignKey(name = "fk_destiny_product"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductEntity destinyProduct;
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    @Column(nullable = false, name = "transaction_date")
    private LocalDateTime transactionDate;

}