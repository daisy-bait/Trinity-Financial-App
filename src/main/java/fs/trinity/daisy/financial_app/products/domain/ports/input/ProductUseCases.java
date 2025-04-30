package fs.trinity.daisy.financial_app.products.domain.ports.input;

import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;

import java.util.List;

public interface ProductUseCases {

    List<ProductModel> getProducts();

    ProductModel getProduct(Long productId);

    ProductModel createProduct(ProductModel productModel);

    ProductModel disableProduct(Long productId);

    ProductModel cancelProduct(Long productId);

    boolean deleteProduct(Long productId);

}
