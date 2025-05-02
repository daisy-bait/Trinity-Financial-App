package fs.trinity.daisy.financial_app.shared.infrastructure.handler;

import fs.trinity.daisy.financial_app.clients.domain.exceptions.AgeNotValidException;
import fs.trinity.daisy.financial_app.shared.infrastructure.message.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {

        Map<String, String> details = doDetails(ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage(),
                        details,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = getValidationErrors(ex.getBindingResult());

        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Not valid arguments fields for " +
                                ex.getTarget().getClass().getSimpleName(),
                        errors,
                        LocalDateTime.now()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        Map<String, String> details = doDetails(ex);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new ErrorResponse(
                        HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        "DTO has unprocessable data in fields, maybe doesn't follows the Fields Format Convention",
                        details,
                        LocalDateTime.now()
                )
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {

        Map<String, String> details = doDetails(ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "Entity not found in DB",
                        details,
                        LocalDateTime.now()
                )
        );
    }

    @ExceptionHandler(AgeNotValidException.class)
    public ResponseEntity<ErrorResponse> handleAgeNotValidException(AgeNotValidException ex) {

        Map<String, String> details = doDetails(ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        details,
                        LocalDateTime.now()
                )
        );
    }

    public Map<String, String> doDetails(Exception ex) {
        Map<String, String> details = new HashMap<>();
        details.put("Exception Cause:", ex.getCause() != null ? ex.getCause().toString() : "There is no thrown information about the exception cause");
        details.put("Exception Name:", ex.getClass().getName());

        return details;
    }

    public Map<String, String> getValidationErrors(BindingResult errors) {
        return errors.getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                FieldError::getDefaultMessage,
                                Collectors.joining(", ")
                        )
                ));
    }

}