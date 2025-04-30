package fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.repository;

import fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {

}
