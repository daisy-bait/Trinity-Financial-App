package fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.repository;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaClientRepository extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByIdentificationNumber(String identificationNumber);

}