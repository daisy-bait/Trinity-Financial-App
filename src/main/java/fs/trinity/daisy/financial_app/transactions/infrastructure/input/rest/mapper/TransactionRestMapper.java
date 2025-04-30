package fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.mapper;

import fs.trinity.daisy.financial_app.shared.infrastructure.mapper.MapperStructure;
import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionModel;
import fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.dto.ResTransactionDTO;
import fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.dto.TransactionDTO;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class TransactionRestMapper extends MapperStructure {

    public TransactionRestMapper() {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.typeMap(TransactionDTO.class, TransactionModel.class)
                .addMapping(TransactionDTO::getOriginProductId, (transactionModel, o) -> transactionModel.getOriginProduct().setId((Long)o))
                .addMapping(TransactionDTO::getDestinyProductId, (transactionModel, o) -> transactionModel.getDestinyProduct().setId((Long)o))
                .addMappings(mapper -> mapper.skip(TransactionModel::setId));
        mapper.typeMap(TransactionModel.class, ResTransactionDTO.class)
                .addMapping((transactionModel) -> transactionModel.getOriginProduct().getId(), ResTransactionDTO::setOriginProductId)
                .addMapping((transactionModel) -> transactionModel.getDestinyProduct().getId(), ResTransactionDTO::setDestinyProductId);
    }

    public TransactionDTO toDTO(TransactionModel transactionModel) {
        return mapper.map(transactionModel, TransactionDTO.class);
    }

    public ResTransactionDTO toResDTO(TransactionModel transactionModel) {
        return mapper.map(transactionModel, ResTransactionDTO.class);
    }

    public TransactionModel toModel(TransactionDTO transactionDTO) {
        return mapper.map(transactionDTO, TransactionModel.class);
    }

}