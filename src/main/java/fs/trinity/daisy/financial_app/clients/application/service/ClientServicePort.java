package fs.trinity.daisy.financial_app.clients.application.service;

import fs.trinity.daisy.financial_app.clients.domain.exceptions.AgeNotValidException;
import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.clients.domain.ports.input.ClientUseCases;
import fs.trinity.daisy.financial_app.clients.domain.ports.output.ClientRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@AllArgsConstructor
@Service
public class ClientServicePort implements ClientUseCases {

    private final ClientRepositoryPort clientRepo;

    @Override
    public List<ClientModel> getClientsModels() {
        return clientRepo.findAll();
    }

    @Override
    public ClientModel getClientModel(Long clientId) {
        return clientRepo.findClientById(clientId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public ClientModel createClient(ClientModel clientModel) {
        clientModel.setCreatedDate(LocalDate.now());
        clientModel.setLastModifiedDate(LocalDate.now());

        if (Period.between(clientModel.getBirthDate(), LocalDate.now()).getYears() < 18) {
            throw new AgeNotValidException("Edad no Válidad Brother");
        }

        return clientRepo.saveClient(clientModel);
    }

    @Override
    public ClientModel modifyClient(ClientModel newClientInfo, Long clientId) {
        ClientModel modifiedClient = this.getClientModel(clientId);
        modifiedClient.setIdType(newClientInfo.getIdType());
        modifiedClient.setIdNum(newClientInfo.getIdNum());
        modifiedClient.setFirstName(newClientInfo.getFirstName());
        modifiedClient.setLastName(newClientInfo.getLastName());
        modifiedClient.setBirthDate(newClientInfo.getBirthDate());
        modifiedClient.setLastModifiedDate(LocalDate.now());
        return clientRepo.saveClient(modifiedClient);
    }

    @Override
    public boolean deleteClient(Long clientId) {
        if (clientRepo.findClientById(clientId).isPresent()) {
            clientRepo.deleteClient(clientId);
            return true;
        }
        return false;
    }
}
