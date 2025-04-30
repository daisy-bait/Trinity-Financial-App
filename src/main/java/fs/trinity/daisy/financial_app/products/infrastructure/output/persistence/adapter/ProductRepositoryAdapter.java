package fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.adapter;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.domain.ports.output.ProductRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    @Override
    public List<ProductModel> findAll() {
        return List.of();
    }

    @Override
    public Optional<ProductModel> findProductById(Long productId) {
        return Optional.empty();
    }

    @Override
    public ProductModel saveProduct(ProductModel clientModel) {
        return null;
    }

    @Override
    public void deleteProductById(Long productId) {

    }
}
