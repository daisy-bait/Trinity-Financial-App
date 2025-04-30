package fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.entity.ClientEntity;
import fs.trinity.daisy.financial_app.products.domain.models.AccountState;
import fs.trinity.daisy.financial_app.products.domain.models.AccountTypes;
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
    private ClientEntity client;
    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate;
    @Column(nullable = false, name = "modified_date")
    private LocalDateTime lastModifiedDate;

}