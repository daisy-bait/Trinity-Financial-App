package fs.trinity.daisy.financial_app.products.infrastructure.input.rest.dto;

import fs.trinity.daisy.financial_app.products.domain.models.AccountState;
import fs.trinity.daisy.financial_app.products.domain.models.AccountTypes;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {

    private Long clientId;
    @NotNull
    private AccountTypes productType;
    private AccountState productState;
    private boolean gmfExempt;

}