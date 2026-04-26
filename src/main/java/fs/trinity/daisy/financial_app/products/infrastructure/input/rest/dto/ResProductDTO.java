package fs.trinity.daisy.financial_app.products.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.dto.ResProductClientDTO;
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
    private ResProductClientDTO client;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm:ss")
    private LocalDateTime lastModifiedDate;

}