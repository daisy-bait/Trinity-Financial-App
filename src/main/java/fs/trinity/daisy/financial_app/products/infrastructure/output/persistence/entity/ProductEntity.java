package fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.entity.ClientEntity;
import fs.trinity.daisy.financial_app.products.domain.models.AccountState;
import fs.trinity.daisy.financial_app.products.domain.models.AccountTypes;
import fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.entity.TransactionEntity;
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
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "product_type")
    private AccountTypes productType;
    @Column(nullable = false, unique = true, name = "product_number")
    private String productNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountState productState;
    private BigDecimal balance;
    @Column(nullable = false, name = "gmf_exempt")
    private boolean gmfExempt;
    @JsonIgnore
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_client_id", foreignKey = @ForeignKey(name = "fk_product_client"))
    private ClientEntity client;
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate;
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    @Column(nullable = false, name = "modified_date")
    private LocalDateTime lastModifiedDate;

}