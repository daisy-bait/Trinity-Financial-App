package fs.trinity.daisy.financial_app.transactions.domain.models;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.products.domain.models.AccountState;
import fs.trinity.daisy.financial_app.products.domain.models.AccountTypes;
import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
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
public class TransactionModel {

    private Long id;
    @Enumerated(EnumType.STRING)
    private TransactionTypes transactionType;
    private BigDecimal amount;
    private ProductModel originProduct;
    private ProductModel destinyProduct;
    private LocalDateTime transactionDate;

}