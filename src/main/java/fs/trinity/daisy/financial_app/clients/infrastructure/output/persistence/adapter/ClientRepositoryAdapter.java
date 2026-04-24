package fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.adapter;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.clients.domain.ports.output.ClientRepositoryPort;
import fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.entity.ClientEntity;
import fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.mapper.ClientPersistenceMapper;
import fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.repository.JpaClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class ClientRepositoryAdapter implements ClientRepositoryPort {

    private final JpaClientRepository clientRepo;

    private final ClientPersistenceMapper mapper;

    @Override
    public List<ClientModel> findAll() {
        return clientRepo.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<ClientModel> findClientById(Long id) {
        return clientRepo.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<ClientModel> findClientByIdNum(String idNum) {
        return clientRepo.findByIdentificationNumber(idNum).map((mapper::toModel));
    }

    @Override
    public ClientModel saveClient(ClientModel client) {
        ClientEntity clientEntity = mapper.toEntity(client);
        return mapper.toModel(clientRepo.save(clientEntity));
    }

    @Override
    public void deleteClient(Long clientId) {
        clientRepo.deleteById(clientId);
    }
}