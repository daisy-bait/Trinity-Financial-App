package fs.trinity.daisy.financial_app.clients.application.service;

import fs.trinity.daisy.financial_app.clients.domain.exceptions.AgeNotValidException;
import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.clients.domain.models.IdTypes;
import fs.trinity.daisy.financial_app.clients.domain.ports.output.ClientRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServicePortTest {

    @Mock
    private ClientRepositoryPort clientRepo;

    @InjectMocks
    private ClientServicePort clientService;

    @Test
    @DisplayName("getClients - Debe retornar lista de clientes")
    void getClients_ShouldReturnClientList() {

        List<ClientModel> expectedClients = List.of(
                createTestClient(1L, "Juan", "Perez"),
                createTestClient(2L, "Maria", "Gomez")
        );

        when(clientRepo.findAll()).thenReturn(expectedClients);

        List<ClientModel> result = clientService.getClients();

        assertEquals(2, result.size());
        assertEquals(expectedClients, result);
        verify(clientRepo).findAll();
    }

    @Test
    @DisplayName("getClient - Debe retornar cliente cuando existe")
    void getClient_WithExistingId_ShouldReturnClient() {

        Long clientId = 1L;
        ClientModel expectedClient = createTestClient(clientId, "Juan", "Perez");

        when(clientRepo.findClientById(clientId)).thenReturn(Optional.of(expectedClient));

        ClientModel result = clientService.getClient(clientId);

        assertEquals(expectedClient, result);
        verify(clientRepo).findClientById(clientId);
    }

    @Test
    @DisplayName("getClient - Debe lanzar excepción cuando cliente no existe")
    void getClient_WithNonExistingId_ShouldThrowException() {

        Long clientId = 99L;

        when(clientRepo.findClientById(clientId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            clientService.getClient(clientId);
        });
        verify(clientRepo).findClientById(clientId);
    }

    @Test
    @DisplayName("createClient - Debe crear cliente válido")
    void createClient_WithValidData_ShouldReturnCreatedClient() {

        ClientModel newClient = createTestClient(null, "Juan", "Perez");
        newClient.setBirthDate(LocalDate.now().minusYears(20));

        ClientModel savedClient = createTestClient(1L, "Juan", "Perez");
        savedClient.setBirthDate(LocalDate.now().minusYears(20));

        when(clientRepo.saveClient(any(ClientModel.class))).thenReturn(savedClient);

        ClientModel result = clientService.createClient(newClient);

        assertNotNull(result.getId());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getLastModifiedDate());
        assertEquals(savedClient, result);
        verify(clientRepo).saveClient(any(ClientModel.class));
    }

    @Test
    @DisplayName("createClient - Debe lanzar excepción cuando edad es menor a 18")
    void createClient_WithUnderageClient_ShouldThrowException() {

        ClientModel underageClient = createTestClient(null, "Nicolle", "Martínez");
        underageClient.setBirthDate(LocalDate.now().minusYears(17));

        assertThrows(AgeNotValidException.class, () -> {
            clientService.createClient(underageClient);
        });
        verify(clientRepo, never()).saveClient(any());
    }

    @Test
    @DisplayName("modifyClient - Debe actualizar cliente existente")
    void modifyClient_WithValidData_ShouldReturnUpdatedClient() {

        Long clientId = 1L;
        ClientModel existingClient = createTestClient(clientId, "Kaleth", "Narváez");
        ClientModel updatedInfo = createTestClient(null, "Kaleth Daniel", "Narváez Paredes");

        when(clientRepo.findClientById(clientId)).thenReturn(Optional.of(existingClient));
        when(clientRepo.saveClient(any(ClientModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClientModel result = clientService.modifyClient(updatedInfo, clientId);

        assertEquals(clientId, result.getId());
        assertEquals("Kaleth Daniel", result.getFirstName());
        assertEquals("Narváez Paredes", result.getLastName());
        assertNotNull(result.getLastModifiedDate());
        verify(clientRepo).findClientById(clientId);
        verify(clientRepo).saveClient(existingClient);
    }

    @Test
    @DisplayName("deleteClient - Debe eliminar cliente existente y retornar true")
    void deleteClient_WithExistingId_ShouldReturnTrue() {

        Long clientId = 1L;
        ClientModel existingClient = createTestClient(clientId, "Kaleth", "Narváez");

        when(clientRepo.findClientById(clientId)).thenReturn(Optional.of(existingClient));
        doNothing().when(clientRepo).deleteClient(clientId);

        boolean result = clientService.deleteClient(clientId);

        assertTrue(result);
        verify(clientRepo).findClientById(clientId);
        verify(clientRepo).deleteClient(clientId);
    }

    @Test
    @DisplayName("deleteClient - Debe retornar false cuando cliente no existe")
    void deleteClient_WithNonExistingId_ShouldReturnFalse() {

        Long clientId = 99L;

        when(clientRepo.findClientById(clientId)).thenReturn(Optional.empty());

        boolean result = clientService.deleteClient(clientId);

        assertFalse(result);
        verify(clientRepo).findClientById(clientId);
        verify(clientRepo, never()).deleteClient(clientId);
    }

    private ClientModel createTestClient(Long id, String firstName, String lastName) {
        ClientModel client = new ClientModel();
        client.setId(id);
        client.setIdType(IdTypes.CIUDADANIA_CEDULA);
        client.setIdNum("1117499277");
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(firstName.toLowerCase() + "@usco.edu.co");
        client.setBirthDate(LocalDate.of(1990, 1, 1));
        client.setCreatedDate(LocalDateTime.now());
        client.setLastModifiedDate(LocalDateTime.now());
        return client;
    }
}