package fs.trinity.daisy.financial_app.products.domain.ports.input;

import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;

import java.util.List;

public interface ProductUseCases {

    List<ProductModel> getProducts();

    ProductModel getProduct(Long productId);

    List<ProductModel> getProductsByClientId(Long clientId);

    ProductModel getProductByProductNumber(String productNumber);

    ProductModel createProduct(ProductModel productModel);

    void updateProduct(ProductModel productModel);

    ProductModel activeProduct(Long productId);

    ProductModel disableProduct(Long productId);

    ProductModel cancelProduct(Long productId);

    ProductModel exemptGMF(Long productId);

    ProductModel disableExemptGMF(Long productId);

    boolean deleteProduct(Long productId);

}
