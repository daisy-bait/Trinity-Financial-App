package fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.adapter;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.clients.domain.ports.output.ClientRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClientRepositoryAdapter implements ClientRepositoryPort {


    @Override
    public List<ClientModel> findAll() {
        return List.of();
    }

    @Override
    public Optional<ClientModel> findClientById(Long id) {
        return Optional.empty();
    }

    @Override
    public ClientModel saveClient(ClientModel client) {
        return null;
    }

    @Override
    public ClientModel updateClient(ClientModel client, Long clientId) {
        return null;
    }

    @Override
    public ClientModel deleteClient(Long clientId) {
        return null;
    }
}
