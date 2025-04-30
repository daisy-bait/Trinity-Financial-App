package fs.trinity.daisy.financial_app.shared.infrastructure.message;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    public Integer statusCode;
    public String message;
    public Map<String, String> details;
    public LocalDateTime timestamp;
}
