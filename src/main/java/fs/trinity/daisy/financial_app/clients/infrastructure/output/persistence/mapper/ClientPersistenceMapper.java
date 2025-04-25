package fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.mapper;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.entity.ClientEntity;
import fs.trinity.daisy.financial_app.shared.infrastructure.mapper.MapperStructure;
import org.springframework.stereotype.Component;

@Component
public class ClientPersistenceMapper extends MapperStructure {

    public ClientPersistenceMapper() {
        mapper.typeMap(ClientEntity.class, ClientModel.class)
                .addMapping(ClientEntity::getIdentificationType, ClientModel::setIdType)
                .addMapping(ClientEntity::getIdentificationNumber, ClientModel::setIdNum)
                .addMapping(ClientEntity::getModifiedDate, ClientModel::setLastModifiedDate);
        mapper.typeMap(ClientModel.class, ClientEntity.class)
                .addMapping(ClientModel::getIdType, ClientEntity::setIdentificationType)
                .addMapping(ClientModel::getIdNum, ClientEntity::setIdentificationNumber)
                .addMapping(ClientModel::getLastModifiedDate, ClientEntity::setModifiedDate);
    }

    public ClientEntity toEntity(ClientModel clientModel) {
        return mapper.map(clientModel, ClientEntity.class);
    }

    public ClientModel toModel(ClientEntity clientEntity) {
        return mapper.map(clientEntity, ClientModel.class);
    }

}
