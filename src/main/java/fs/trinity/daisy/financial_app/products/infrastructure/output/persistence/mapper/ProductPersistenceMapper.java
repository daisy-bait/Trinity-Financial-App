package fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.mapper;

import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.entity.ProductEntity;
import fs.trinity.daisy.financial_app.shared.infrastructure.mapper.MapperStructure;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceMapper extends MapperStructure {

    public ProductEntity toEntity(ProductModel productModel) {
        return mapper.map(productModel, ProductEntity.class);
    }

    public ProductModel toModel(ProductEntity productEntity) {
        return mapper.map(productEntity, ProductModel.class);
    }

}