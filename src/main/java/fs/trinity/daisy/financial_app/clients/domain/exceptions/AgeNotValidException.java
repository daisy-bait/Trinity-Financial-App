package fs.trinity.daisy.financial_app.clients.domain.exceptions;

public class AgeNotValidException extends RuntimeException {
    public AgeNotValidException(String message) {
        super(message);
    }
}
