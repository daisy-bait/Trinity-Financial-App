package fs.trinity.daisy.financial_app.shared.infrastructure.handler;

import fs.trinity.daisy.financial_app.shared.infrastructure.message.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {

        Map<String, String> details = doDetails(ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        details,
                        LocalDateTime.now()
                )
        );
    }

    public Map<String, String> doDetails(Exception ex) {
        Map<String, String> details = new HashMap<>();
        details.put("message:", ex.getLocalizedMessage());
        details.put("cause:", ex.getCause() != null ? ex.getCause().toString() : "There is no info");

        return details;
    }

}
