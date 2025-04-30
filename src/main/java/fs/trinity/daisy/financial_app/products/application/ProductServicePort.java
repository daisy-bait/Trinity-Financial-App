package fs.trinity.daisy.financial_app.products.application;

import fs.trinity.daisy.financial_app.products.domain.models.AccountState;
import fs.trinity.daisy.financial_app.products.domain.models.AccountTypes;
import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.domain.ports.input.ProductUseCases;
import fs.trinity.daisy.financial_app.products.domain.ports.output.ProductRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Service
public class ProductServicePort implements ProductUseCases {

    private final ProductRepositoryPort productRepo;

    @Override
    public List<ProductModel> getProducts() {
        return productRepo.findAll();
    }

    @Override
    public ProductModel getProduct(Long productId) {
        return productRepo.findProductById(productId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public ProductModel createProduct(ProductModel productModel) {
        productModel.setCreatedDate(LocalDateTime.now());
        productModel.setLastModifiedDate(LocalDateTime.now());
        generateProductNumber(productModel);

        if (productModel.getProductType().equals(AccountTypes.AHORROS) && productModel.getState() == null)
            productModel.setState(AccountState.ACTIVE);

        return productRepo.saveProduct(productModel);
    }

    @Override
    public ProductModel disableProduct(Long productId) {
        ProductModel modifiedProduct = this.getProduct(productId);
        if (modifiedProduct.getState().equals(AccountState.INACTIVE))
            throw new RuntimeException("Product has already been disabled");

        modifiedProduct.setState(AccountState.INACTIVE);

        return productRepo.saveProduct(modifiedProduct);
    }

    @Override
    public ProductModel cancelProduct(Long productId) {
        ProductModel modifiedProduct = this.getProduct(productId);
        if (modifiedProduct.getState().equals(AccountState.CANCELLED)) {
            throw new RuntimeException("Product has already been cancelled");
        } else if (!modifiedProduct.getBalance().equals(0.00)) {
            throw new RuntimeException("Product has balance grater than 0");
        }

        modifiedProduct.setState(AccountState.CANCELLED);

        return productRepo.saveProduct(modifiedProduct);
    }

    @Override
    public boolean deleteProduct(Long productId) {
        if (productRepo.findProductById(productId).isPresent()) {
            ProductModel modifiedProduct = this.getProduct(productId);
            if (modifiedProduct.getState().equals(AccountState.CANCELLED)) {
                productRepo.deleteProductById(productId);
                return true;
            }
            throw new RuntimeException("Product isn't deleted");
        }
        return false;
    }

    public String generateProductNumber(ProductModel product) {
        String prefix = (product.getProductType().equals(AccountTypes.AHORROS) ? "53" : "33");
        String randomNumbers = new String();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            randomNumbers.concat(random.nextInt(10) + "");
        }

        return prefix.concat(randomNumbers);
    }

}