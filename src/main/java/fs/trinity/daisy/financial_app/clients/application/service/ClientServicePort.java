package fs.trinity.daisy.financial_app.clients.application.service;

import fs.trinity.daisy.financial_app.clients.domain.exceptions.AgeNotValidException;
import fs.trinity.daisy.financial_app.clients.domain.exceptions.ClientAlreadyExistsException;
import fs.trinity.daisy.financial_app.clients.domain.exceptions.NotFoundClientException;
import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.clients.domain.ports.input.ClientUseCases;
import fs.trinity.daisy.financial_app.clients.domain.ports.output.ClientRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@AllArgsConstructor
@Service
public class ClientServicePort implements ClientUseCases {

    private final ClientRepositoryPort clientRepo;

    @Override
    public List<ClientModel> getClients() {
        return clientRepo.findAll();
    }

    @Override
    public ClientModel getClient(Long clientId) {
        return clientRepo.findClientById(clientId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public ClientModel getClientByIdNum(String idNum) {
        return clientRepo.findClientByIdNum(idNum)
                .orElseThrow(NotFoundClientException::new);
    }

    @Override
    public ClientModel createClient(ClientModel clientModel) {
        clientModel.setCreatedDate(LocalDateTime.now());
        clientModel.setLastModifiedDate(LocalDateTime.now());

        if (clientRepo.findClientByIdNum(clientModel.getIdNum()).isPresent()) {
            throw new ClientAlreadyExistsException();
        }

        validateBirthAge(clientModel.getBirthDate());

        return clientRepo.saveClient(clientModel);
    }

    @Override
    public ClientModel modifyClient(ClientModel newClientInfo, Long clientId) {
        validateBirthAge(newClientInfo.getBirthDate());

        ClientModel modifiedClient = this.getClient(clientId);
        modifiedClient.setIdType(newClientInfo.getIdType());
        modifiedClient.setIdNum(newClientInfo.getIdNum());
        modifiedClient.setFirstName(newClientInfo.getFirstName());
        modifiedClient.setLastName(newClientInfo.getLastName());
        modifiedClient.setEmail(newClientInfo.getEmail());
        modifiedClient.setBirthDate(newClientInfo.getBirthDate());
        modifiedClient.setLastModifiedDate(LocalDateTime.now());

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

    private void validateBirthAge(LocalDate birthDate) {
        if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
            throw new AgeNotValidException("Debes ser mayor de edad.");
        }
    }

}