package fs.trinity.daisy.financial_app.products.infrastructure.input.rest.mapper;

import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.infrastructure.input.rest.dto.ProductDTO;
import fs.trinity.daisy.financial_app.products.infrastructure.input.rest.dto.ResProductDTO;
import fs.trinity.daisy.financial_app.shared.infrastructure.mapper.MapperStructure;
import org.springframework.stereotype.Component;

@Component
public class ProductRestMapper extends MapperStructure {

    public ProductRestMapper() {
        this.mapper.typeMap(ProductDTO.class, ProductModel.class)
                .addMappings(mapper -> mapper.skip(ProductModel::setId));
    }

    public ProductDTO toDTO(ProductModel productModel) {
        return mapper.map(productModel, ProductDTO.class);
    }

    public ResProductDTO toResDTO(ProductModel productModel) {
        return mapper.map(productModel, ResProductDTO.class);
    }

    public ProductModel toModel(ProductDTO productDTO) {
        return mapper.map(productDTO, ProductModel.class);
    }

}