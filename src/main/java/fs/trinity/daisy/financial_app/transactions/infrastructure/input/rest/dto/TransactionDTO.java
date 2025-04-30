package fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionDTO {

    @NotNull
    private BigDecimal amount;
    @NotNull
    private Long originProductId;
    private Long destinyProductId;

}
