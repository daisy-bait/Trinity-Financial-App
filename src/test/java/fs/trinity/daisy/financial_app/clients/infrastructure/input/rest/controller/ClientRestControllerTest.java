package fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.controller;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.clients.domain.models.IdTypes;
import fs.trinity.daisy.financial_app.clients.domain.ports.input.ClientUseCases;
import fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.dto.ClientDTO;
import fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.dto.ResClientDTO;
import fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.mapper.ClientRestMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientRestControllerTest {

    @Mock
    private ClientUseCases clientServicePort;

    @Mock
    private ClientRestMapper mapper;

    @InjectMocks
    private ClientRestController clientController;

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.afterPropertiesSet();
        validator = factory;
    }

    @Test
    @DisplayName("GET /find-all - Debe retornar lista de clientes mapeados")
    void retrieveClients_ShouldReturnListOfClients() {
        List<ClientModel> clients = List.of(new ClientModel(), new ClientModel());
        List<ResClientDTO> expectedDtos = List.of(new ResClientDTO(), new ResClientDTO());

        when(clientServicePort.getClients()).thenReturn(clients);
        when(mapper.toResDTO(any(ClientModel.class))).thenReturn(expectedDtos.get(0), expectedDtos.get(1));

        ResponseEntity<List<ResClientDTO>> response = clientController.retrieveClients();

        assertEquals(expectedDtos, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(clientServicePort).getClients();
        verify(mapper, times(2)).toResDTO(any(ClientModel.class));

        testPasadoInge();
    }

    @Test
    @DisplayName("GET /find/{id} - Debe retornar cliente cuando ID existe")
    void retrieveClientById_WithValidId_ShouldReturnClient() {
        Long clientId = 1L;
        ClientModel client = new ClientModel();
        ResClientDTO expectedDto = new ResClientDTO();

        when(clientServicePort.getClient(clientId)).thenReturn(client);
        when(mapper.toResDTO(client)).thenReturn(expectedDto);

        ResponseEntity<ResClientDTO> response = clientController.retrieveClientById(clientId);

        assertEquals(expectedDto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(clientServicePort).getClient(clientId);
        verify(mapper).toResDTO(client);

        testPasadoInge();
    }

    @Test
    @DisplayName("GET /find/{id} - Debe lanzar excepción cuando ID no existe")
    void retrieveClientById_WithNonExistingId_ShouldThrowException() {
        Long clientId = 99L;

        when(clientServicePort.getClient(clientId)).thenThrow(new RuntimeException("Client not found"));

        assertThrows(RuntimeException.class, () -> {
            clientController.retrieveClientById(clientId);
        });

        testPasadoInge();
    }

    @Test
    @DisplayName("POST /save - Debe retornar cliente creado con datos válidos")
    void saveClient_WithValidClientDTO_ShouldReturnCreated() {

        ClientDTO validClientDTO = createValidClientDTO();
        ClientModel validClient = new ClientModel();
        ResClientDTO expectedResponse = new ResClientDTO();

        when(mapper.toModel(validClientDTO)).thenReturn(validClient);
        when(clientServicePort.createClient(validClient)).thenReturn(validClient);
        when(mapper.toResDTO(validClient)).thenReturn(expectedResponse);

        ResponseEntity<ResClientDTO> response = clientController.saveClient(validClientDTO);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        testPasadoInge();
    }

    @Test
    @DisplayName("POST /save - Debe retornar excepción con datos no válidos")
    void saveClient_WithInvalidFields_ShouldThrowValidationException() {

        ClientDTO invalidClientDTO = createValidClientDTO();

        //Tests for IdNum
        invalidClientDTO.setIdNum(null);
        assertValidationFails(invalidClientDTO, "idNum", "no debe ser nulo");
        invalidClientDTO.setIdNum("1234567"); // 7 dígitos
        assertValidationFails(invalidClientDTO, "idNum", "Must be a number with a length from 8 to 10");
        invalidClientDTO.setIdNum("12345678901"); // 11 dígitos
        assertValidationFails(invalidClientDTO, "idNum", "Must be a number with a length from 8 to 10");
        invalidClientDTO.setIdNum("123ABC456"); // contiene letras
        assertValidationFails(invalidClientDTO, "idNum", "Must be a number with a length from 8 to 10");

        //Tests for FirstName
        invalidClientDTO.setFirstName(null);
        assertValidationFails(invalidClientDTO, "firstName", "no debe ser nulo");
        invalidClientDTO.setFirstName("K"); // muy corto
        assertValidationFails(invalidClientDTO, "firstName", "Must containt at least 1 word with 2 characters");
        invalidClientDTO.setFirstName("juan"); // minúscula inicial
        assertValidationFails(invalidClientDTO, "firstName", "Must containt at least 1 word with 2 characters");
        invalidClientDTO.setFirstName("Juan1"); // contiene número
        assertValidationFails(invalidClientDTO, "firstName", "Must containt at least 1 word with 2 characters");
        invalidClientDTO.setFirstName("Juan Pedro Pérez"); // más de 2 palabras
        assertValidationFails(invalidClientDTO, "firstName", "Must containt at least 1 word with 2 characters");

        //Tests for LastName (mismas validaciones que firstName)
        invalidClientDTO.setLastName(null);
        assertValidationFails(invalidClientDTO, "lastName", "no debe ser nulo");
        invalidClientDTO.setLastName("P"); // muy corto
        assertValidationFails(invalidClientDTO, "lastName", "Must containt at least 1 word with 2 characters");
        invalidClientDTO.setLastName("pérez"); // minúscula inicial
        assertValidationFails(invalidClientDTO, "lastName", "Must containt at least 1 word with 2 characters");
        invalidClientDTO.setLastName("Pérez2"); // contiene número
        assertValidationFails(invalidClientDTO, "lastName", "Must containt at least 1 word with 2 characters");

        //Tests for BirthDate
        invalidClientDTO.setBirthDate(null);
        assertValidationFails(invalidClientDTO, "birthDate", "no debe ser nulo");
        // Nota: La validación del formato dd.MM.yyyy se hace automáticamente con @JsonFormat

        //Tests for EmailAddress
        invalidClientDTO.setEmailAddress(null);
        assertValidationFails(invalidClientDTO, "emailAddress", "no debe ser nulo");
        invalidClientDTO.setEmailAddress("juan.example.com"); // sin @
        assertValidationFails(invalidClientDTO, "emailAddress", "Must be a valid E-mail Address");
        invalidClientDTO.setEmailAddress("juan@example"); // sin dominio completo
        assertValidationFails(invalidClientDTO, "emailAddress", "Must be a valid E-mail Address");
        invalidClientDTO.setEmailAddress("juan perez@example.com"); // con espacios
        assertValidationFails(invalidClientDTO, "emailAddress", "Must be a valid E-mail Address");

        testPasadoInge();
    }

    @Test
    @DisplayName("PUT /modify/{id} - Debe retornar cliente actualizado")
    void updateClient_WithValidData_ShouldReturnUpdatedClient() {
        Long clientId = 1L;
        ClientDTO clientDTO = createValidClientDTO();
        ClientModel client = new ClientModel();
        ResClientDTO expectedDto = new ResClientDTO();

        when(mapper.toModel(clientDTO)).thenReturn(client);
        when(clientServicePort.modifyClient(client, clientId)).thenReturn(client);
        when(mapper.toResDTO(client)).thenReturn(expectedDto);

        ResponseEntity<ResClientDTO> response = clientController.updateClient(clientDTO, clientId);

        assertEquals(expectedDto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(clientServicePort).modifyClient(client, clientId);
        verify(mapper).toModel(clientDTO);
        verify(mapper).toResDTO(client);

        testPasadoInge();
    }

    @Test
    @DisplayName("DELETE /delete/{id} - Debe retornar true cuando elimina cliente existente")
    void deleteClient_WithValidId_ShouldReturnTrue() {
        Long clientId = 1L;

        when(clientServicePort.deleteClient(clientId)).thenReturn(true);

        ResponseEntity<Boolean> response = clientController.deleteClient(clientId);

        assertTrue(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(clientServicePort).deleteClient(clientId);

        testPasadoInge();
    }

    @Test
    @DisplayName("DELETE /delete/{id} - Debe retornar false cuando ID no existe")
    void deleteClient_WithNonExistingId_ShouldReturnFalse() {
        Long clientId = 99L;

        when(clientServicePort.deleteClient(clientId)).thenReturn(false);

        ResponseEntity<Boolean> response = clientController.deleteClient(clientId);

        assertFalse(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        testPasadoInge();
    }

    private void assertValidationFails(ClientDTO dto, String property, String message) {
        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(property)
                        && v.getMessage().contains(message)));
    }

    private void testPasadoInge() {
        System.out.println("Test pasado Papu");
    }

    private ClientDTO createValidClientDTO() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setIdType(IdTypes.CIUDADANIA_CEDULA);
        clientDTO.setIdNum("1117499277");
        clientDTO.setFirstName("Kaleth Daniel");
        clientDTO.setLastName("Narváez Paredes");
        clientDTO.setBirthDate(LocalDate.of(2006, 4, 4));
        clientDTO.setEmailAddress("kadanarpa@gmail.com");
        return clientDTO;
    }
}