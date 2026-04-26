package fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fs.trinity.daisy.financial_app.products.domain.models.AccountTypes;
import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionTypes;
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
public class ResTransactionDTO extends TransactionDTO {

    private Long id;
    private TransactionTypes transactionType;
    private BigDecimal amount;
    private Long originProductId;
    private Long destinyProductId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm:ss")
    private LocalDateTime transactionDate;

}
