package fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fs.trinity.daisy.financial_app.clients.domain.models.IdTypes;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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

    @NotNull
    private IdTypes idType;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 0)
    private Integer idNum;

    @NotNull
    @Pattern(regexp = "^[A-ZÑ][A-Za-zÀ-ÿ]+(\\s[A-ZÑ][A-Za-zÀ-ÿ]+){0,1}$")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[A-ZÑ][A-Za-zÀ-ÿ]+(\\s[A-ZÑ][A-Za-zÀ-ÿ]+){0,1}$")
    private String lastName;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    @NotNull
    @Pattern(regexp = "^[\\w.%+-]+@[A-Za-z\\d\\.-]{2,}\\.([a-z]{2,6})+$")
    private String emailAddress;

}