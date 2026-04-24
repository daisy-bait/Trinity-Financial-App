package fs.trinity.daisy.financial_app.clients.domain.ports.output;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;

import java.util.List;
import java.util.Optional;

public interface ClientRepositoryPort {

    List<ClientModel> findAll();

    Optional<ClientModel> findClientById(Long id);

    Optional<ClientModel> findClientByIdNum(String idNum);

    ClientModel saveClient(ClientModel client);

    void deleteClient(Long clientId);

}
