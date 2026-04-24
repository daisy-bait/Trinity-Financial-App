package fs.trinity.daisy.financial_app.products.application.service;

import fs.trinity.daisy.financial_app.clients.domain.ports.input.ClientUseCases;
import fs.trinity.daisy.financial_app.products.domain.models.AccountState;
import fs.trinity.daisy.financial_app.products.domain.models.AccountTypes;
import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.domain.ports.input.ProductUseCases;
import fs.trinity.daisy.financial_app.products.domain.ports.output.ProductRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@Service
public class ProductServicePort implements ProductUseCases {

    private final ProductRepositoryPort productRepo;

    private final ClientUseCases clientServicePort;

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
    public ProductModel getProductByClientId(Long clientId) {
        return productRepo.findProductById(clientId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public ProductModel createProduct(ProductModel productModel) {
        productModel.setCreatedDate(LocalDateTime.now());
        productModel.setLastModifiedDate(LocalDateTime.now());
        productModel.setBalance(BigDecimal.ZERO);

        productModel.setClient(clientServicePort.getClient(productModel.getClient().getId()));

        generateProductNumber(productModel);

        if (productModel.getProductType().equals(AccountTypes.AHORROS) && productModel.getProductState() == null) {
            productModel.setProductState(AccountState.ACTIVE);
        } else if (productModel.getProductState() == null){
            productModel.setProductState(AccountState.INACTIVE);
        }

        return productRepo.saveProduct(productModel);
    }

    @Override
    public void updateProduct(ProductModel productModel) {
        productModel.setLastModifiedDate(LocalDateTime.now());
        productRepo.saveProduct(productModel);
    }

    @Override
    public ProductModel activeProduct(Long productId) {
        ProductModel modifiedProduct = this.getProduct(productId);
        if (modifiedProduct.getProductState().equals(AccountState.ACTIVE))
            throw new RuntimeException("Product has already been actived");

        modifiedProduct.setProductState(AccountState.ACTIVE);
        modifiedProduct.setLastModifiedDate(LocalDateTime.now());

        return productRepo.saveProduct(modifiedProduct);
    }

    @Override
    public ProductModel disableProduct(Long productId) {
        ProductModel modifiedProduct = this.getProduct(productId);
        if (modifiedProduct.getProductState().equals(AccountState.INACTIVE))
            throw new RuntimeException("Product has already been disabled");

        modifiedProduct.setProductState(AccountState.INACTIVE);
        modifiedProduct.setLastModifiedDate(LocalDateTime.now());

        return productRepo.saveProduct(modifiedProduct);
    }

    @Override
    public ProductModel cancelProduct(Long productId) {
        ProductModel modifiedProduct = this.getProduct(productId);
        if (modifiedProduct.getProductState().equals(AccountState.CANCELLED)) {
            throw new RuntimeException("Product has already been cancelled");
        } else if (modifiedProduct.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Product balance isn't equal to 0");
        }

        modifiedProduct.setProductState(AccountState.CANCELLED);
        modifiedProduct.setLastModifiedDate(LocalDateTime.now());

        return productRepo.saveProduct(modifiedProduct);
    }

    @Override
    public ProductModel exemptGMF(Long productId) {
        ProductModel modifiedProduct = this.getProduct(productId);
        if (modifiedProduct.isGmfExempt()) {
            throw new RuntimeException("Product has already been exempted");
        }
        verifyIsProductIsAvailableToGMF(modifiedProduct.getClient().getId(), productId);
        modifiedProduct.setGmfExempt(true);
        modifiedProduct.setLastModifiedDate(LocalDateTime.now());

        return productRepo.saveProduct(modifiedProduct);
    }

    @Override
    public ProductModel disableExemptGMF(Long productId) {
        ProductModel modifiedProduct = this.getProduct(productId);
        if (!modifiedProduct.isGmfExempt()) {
            throw new RuntimeException("Product has already been disabled to gmf exempt");
        }
        modifiedProduct.setGmfExempt(false);
        modifiedProduct.setLastModifiedDate(LocalDateTime.now());

        return productRepo.saveProduct(modifiedProduct);
    }

    @Override
    public boolean deleteProduct(Long productId) {
        if (productRepo.findProductById(productId).isPresent()) {
            ProductModel toDeleteProduct = this.getProduct(productId);
            if (toDeleteProduct.getProductState().equals(AccountState.CANCELLED)) {
                productRepo.deleteProductById(productId);
                return true;
            }
            throw new RuntimeException("Product isn't cancelled");
        }
        return false;
    }

    public void generateProductNumber(ProductModel product) {
        String prefix = (product.getProductType().equals(AccountTypes.AHORROS) ? "53" : "33");
        String randomNumbers = "";
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            String randomNumber = random.nextInt(10) + "";
            randomNumbers += randomNumber;
        }

        product.setProductNumber(prefix.concat(randomNumbers));
    }

    public void verifyIsProductIsAvailableToGMF(Long clientId, Long productId) {
        Optional<ProductModel> product = productRepo.verifyIfIsAvailableToGmfExempt(clientId, productId);
        if (productRepo.verifyIfIsAvailableToGmfExempt(clientId, productId).isPresent()) {
            throw new RuntimeException("Product is not available to GMF");
        }
    }

}