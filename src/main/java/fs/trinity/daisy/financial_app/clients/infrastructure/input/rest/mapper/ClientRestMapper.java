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
                .addMapping(ResClientDTO::getUpdatedAt, ClientModel::setLastModifiedDate);
        this.mapper.typeMap(ClientModel.class, ResClientDTO.class)
                .addMapping(ClientModel::getCreatedDate , ResClientDTO::setCreatedAt)
                .addMapping(ClientModel::getLastModifiedDate, ResClientDTO::setUpdatedAt);
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