package fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.repository;

import fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.entity.ClientEntity;
import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query(value = "SELECT p FROM ProductEntity p " +
            "WHERE p.client.id = :client_id " +
            "AND p.gmfExempt = true " +
            "AND p.id != :product_id")
    Optional<ProductEntity> findProductWithGmfExempt(@Param("client_id") Long clientId, @Param("product_id") Long productId);

    Optional<ProductEntity> findByClientId(Long clientId);

}
