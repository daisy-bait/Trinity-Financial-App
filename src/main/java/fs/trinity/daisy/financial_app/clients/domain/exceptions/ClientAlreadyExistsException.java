package fs.trinity.daisy.financial_app.clients.domain.exceptions;

public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException() {
        super("El Cliente ya existe.");
    }
}
