package fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.repository;

import fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaClientRepository extends JpaRepository<ClientEntity, Long> {


}