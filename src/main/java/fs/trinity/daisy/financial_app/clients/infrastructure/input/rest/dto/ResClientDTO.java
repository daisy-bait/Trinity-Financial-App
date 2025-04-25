package fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResClientDTO extends ClientDTO {

    private Long id;
    private LocalDate createdAt;
    private LocalDate updatedAt;

}
