package fs.trinity.daisy.financial_app.clients.domain.models;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientModel {

    private Long id;
    @Enumerated(EnumType.STRING)
    private IdTypes idType;
    private Integer idNum;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;

}
