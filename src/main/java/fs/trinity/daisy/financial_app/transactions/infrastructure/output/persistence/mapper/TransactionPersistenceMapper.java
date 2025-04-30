package fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.mapper;

import fs.trinity.daisy.financial_app.shared.infrastructure.mapper.MapperStructure;
import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionModel;
import fs.trinity.daisy.financial_app.transactions.infrastructure.output.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionPersistenceMapper extends MapperStructure {

    public TransactionEntity toEntity(TransactionModel transactionModel) {
        return mapper.map(transactionModel, TransactionEntity.class);
    }

    public TransactionModel toModel(TransactionEntity transactionEntity) {
        return mapper.map(transactionEntity, TransactionModel.class);
    }

}
