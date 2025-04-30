package fs.trinity.daisy.financial_app.products.domain.ports.output;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    List<ClientModel> findAll();

    Optional<ClientModel> findProductById(Long productId);

    ClientModel saveClient(ClientModel clientModel);

    void deleteClientById(Long productId);

}
