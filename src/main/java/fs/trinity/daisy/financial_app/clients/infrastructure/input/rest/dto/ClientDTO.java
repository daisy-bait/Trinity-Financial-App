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
    @Pattern(regexp = "^\\d{8,10}$", message = "Must be a number with a length from 8 to 10")
    private String idNum;

    @NotNull
    @Pattern(regexp = "^[A-ZÑ][A-Za-zÀ-ÿ]+(\\s[A-ZÑ][A-Za-zÀ-ÿ]+){0,1}$", message = "Must containt at least 1 word with 2 characters, and max 2 words, without numbers")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[A-ZÑ][A-Za-zÀ-ÿ]+(\\s[A-ZÑ][A-Za-zÀ-ÿ]+){0,1}$", message = "Must containt at least 1 word with 2 characters, and max 2 words, without numbers")
    private String lastName;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    @NotNull
    @Pattern(regexp = "^[\\w.%+-]+@[A-Za-z\\d\\.-]{2,}\\.([a-z]{2,6})+$", message = "Must be a valid E-mail Address")
    private String emailAddress;

}