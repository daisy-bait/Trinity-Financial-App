package fs.trinity.daisy.financial_app.clients.infrastructure.output.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fs.trinity.daisy.financial_app.clients.domain.models.IdTypes;
import fs.trinity.daisy.financial_app.products.infrastructure.output.persistence.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "clients")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "identification_type")
    @Enumerated(EnumType.STRING)
    private IdTypes identificationType;
    @Column(nullable = false, unique = true, name = "identification_number")
    private Integer identificationNumber;
    @Column(nullable = false, name = "first_name")
    private String firstName;
    @Column(nullable = false, name = "last_name")
    private String lastName;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(nullable = false, name = "birth_date")
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    private LocalDate birthDate;
    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate;
    @Column(nullable = false, name = "modified_date")
    private LocalDateTime modifiedDate;
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products;

}