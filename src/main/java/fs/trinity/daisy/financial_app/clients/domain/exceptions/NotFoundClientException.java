package fs.trinity.daisy.financial_app.clients.domain.exceptions;

public class NotFoundClientException extends RuntimeException {
    public NotFoundClientException() {
        super("Cliente no encontrado.");
    }
}
