package fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.dto;

import fs.trinity.daisy.financial_app.clients.domain.models.IdTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientDTO {

    private IdTypes idType;
    private L
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

}
