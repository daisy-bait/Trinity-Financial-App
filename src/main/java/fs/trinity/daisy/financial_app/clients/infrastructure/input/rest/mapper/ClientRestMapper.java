package fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.mapper;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.dto.ClientDTO;
import fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.dto.ResClientDTO;
import fs.trinity.daisy.financial_app.shared.infrastructure.mapper.MapperStructure;
import org.springframework.stereotype.Component;

@Component
public class ClientRestMapper extends MapperStructure {

    public ClientRestMapper() {

        this.mapper.typeMap(ResClientDTO.class, ClientModel.class)
                .addMapping(ResClientDTO::getCreatedAt, ClientModel::setCreatedDate)
                .addMapping(ResClientDTO::getUpdatedAt, ClientModel::setLastModifiedDate)
                .addMapping(ResClientDTO::getEmailAddress, ClientModel::setEmail);
        this.mapper.typeMap(ClientModel.class, ResClientDTO.class)
                .addMapping(ClientModel::getCreatedDate , ResClientDTO::setCreatedAt)
                .addMapping(ClientModel::getLastModifiedDate, ResClientDTO::setUpdatedAt)
                .addMapping(ClientModel::getEmail, ResClientDTO::setEmailAddress);

        this.mapper.typeMap(ClientDTO.class, ClientModel.class)
                .addMapping(ClientDTO::getEmailAddress, ClientModel::setEmail)
                .addMappings(mapper -> mapper.skip(ClientModel::setId));

        this.mapper.typeMap(ClientModel.class, ClientDTO.class)
                .addMapping(ClientModel::getEmail, ClientDTO::setEmailAddress);

    }

    public ClientDTO toDTO(ClientModel clientModel) {
        return mapper.map(clientModel, ClientDTO.class);
    }

    public ResClientDTO toResDTO(ClientModel clientModel) {
        return mapper.map(clientModel, ResClientDTO.class);
    }

    public ClientModel toModel(ClientDTO clientDTO) {
        return mapper.map(clientDTO, ClientModel.class);
    }

}