package fs.trinity.daisy.financial_app.products.domain.models;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class ProductModel {

    private Long id;
    @Enumerated(EnumType.STRING)
    private AccountTypes productType;
    private String productNumber;
    @Enumerated(EnumType.STRING)
    private AccountState productState;
    private BigDecimal balance;
    private boolean gmfExempt;
    private ClientModel client;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}