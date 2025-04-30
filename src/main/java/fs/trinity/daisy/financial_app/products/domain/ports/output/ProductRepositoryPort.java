package fs.trinity.daisy.financial_app.products.domain.ports.output;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    List<ProductModel> findAll();

    Optional<ProductModel> findProductById(Long productId);

    ProductModel saveProduct(ProductModel clientModel);

    void deleteProductById(Long productId);

    Optional<ProductModel> verifyIfIsAvailableToGmfExempt(Long clientId, Long productId);

}
