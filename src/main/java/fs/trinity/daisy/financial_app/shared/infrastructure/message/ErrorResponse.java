package fs.trinity.daisy.financial_app.shared.infrastructure.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    public Integer statusCode;
    public String message;
    public Map<String, String> details;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm:ss")
    public LocalDateTime timestamp;
}
