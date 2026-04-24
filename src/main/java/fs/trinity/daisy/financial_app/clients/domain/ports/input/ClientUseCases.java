package fs.trinity.daisy.financial_app.clients.domain.ports.input;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;

import java.util.List;

public interface ClientUseCases {

    List<ClientModel> getClients();

    ClientModel getClient(Long clientId);

    ClientModel getClientByIdNum(String clientId);

    ClientModel createClient(ClientModel clientModel);

    ClientModel modifyClient(ClientModel toModifyClient, Long clientId);

    boolean deleteClient(Long clientId);

}
