package fs.trinity.daisy.financial_app.products.infrastructure.input.rest.dto;

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
public class ResProductDTO extends ProductDTO {

    private Long id;
    private String productNumber;
    private BigDecimal balance;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}