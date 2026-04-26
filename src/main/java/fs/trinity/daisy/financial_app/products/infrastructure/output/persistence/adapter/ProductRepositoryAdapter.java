package fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.adapter;

import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.domain.ports.output.ProductRepositoryPort;
import fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.entity.ProductEntity;
import fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.mapper.ProductPersistenceMapper;
import fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.repository.JpaProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final JpaProductRepository productRepo;

    private final ProductPersistenceMapper mapper;

    @Override
    public List<ProductModel> findAll() {
        return productRepo.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<ProductModel> findProductById(Long productId) {
        return productRepo.findById(productId).map(mapper::toModel);
    }

    @Override
    public List<ProductModel> findProductsByClientId(Long clientId) {
        return productRepo.findByClientId(clientId).stream().map(mapper::toModel).toList();
    }

    @Override
    public Page<ProductModel> pageProductByProductNumber(Pageable pageable, String productNumber) {
        return productRepo.pageByProductNumber(productNumber, pageable).map(mapper::toModel);
    }

    @Override
    public ProductModel saveProduct(ProductModel productModel) {
        ProductEntity productEntity = mapper.toEntity(productModel);
        return mapper.toModel(productRepo.save(productEntity));
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepo.deleteById(productId);
    }

    @Override
    public Optional<ProductModel> verifyIfIsAvailableToGmfExempt(Long clientId, Long productId) {
        return productRepo.findProductWithGmfExempt(clientId, productId).map(mapper::toModel);
    }

}
