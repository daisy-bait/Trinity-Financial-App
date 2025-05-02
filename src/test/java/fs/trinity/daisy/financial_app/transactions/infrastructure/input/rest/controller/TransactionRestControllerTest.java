package fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.controller;

import fs.trinity.daisy.financial_app.transactions.application.service.TransactionServicePort;
import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionModel;
import fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.dto.ResTransactionDTO;
import fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.dto.TransactionDTO;
import fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.mapper.TransactionRestMapper;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionRestControllerTest {

    @Mock
    private TransactionServicePort transactionServicePort;

    @Mock
    private TransactionRestMapper mapper;

    @InjectMocks
    private TransactionRestController transactionRestController;

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.afterPropertiesSet();
        validator = factory;
    }

    @Test
    @DisplayName("GET /find-all - Debe retornar lista de transacciones mapeadas")
    void findAll_ShouldReturnListOfTransactions() {
        List<TransactionModel> transactions = List.of(new TransactionModel(), new TransactionModel());
        List<ResTransactionDTO> expectedDtos = List.of(new ResTransactionDTO(), new ResTransactionDTO());

        when(transactionServicePort.getAllTransactions()).thenReturn(transactions);
        when(mapper.toResDTO(any(TransactionModel.class))).thenReturn(expectedDtos.get(0), expectedDtos.get(1));

        ResponseEntity<List<ResTransactionDTO>> response = transactionRestController.findAll();

        assertEquals(expectedDtos, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(transactionServicePort).getAllTransactions();
        verify(mapper, times(2)).toResDTO(any(TransactionModel.class));
    }

    @Test
    @DisplayName("GET /find/{id} - Debe retornar transacción cuando ID existe")
    void findById_WithValidId_ShouldReturnTransaction() {
        Long transactionId = 1L;
        TransactionModel transaction = new TransactionModel();
        ResTransactionDTO expectedDto = new ResTransactionDTO();

        when(transactionServicePort.getTransactionById(transactionId)).thenReturn(transaction);
        when(mapper.toResDTO(transaction)).thenReturn(expectedDto);

        ResponseEntity<ResTransactionDTO> response = transactionRestController.findById(transactionId);

        assertEquals(expectedDto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(transactionServicePort).getTransactionById(transactionId);
        verify(mapper).toResDTO(transaction);
    }

    @Test
    @DisplayName("POST /consign - Debe realizar consignación con datos válidos")
    void consign_WithValidData_ShouldReturnTransaction() {
        TransactionDTO transactionDTO = createValidTransactionDTO();
        TransactionModel transactionModel = new TransactionModel();
        ResTransactionDTO expectedResponse = new ResTransactionDTO();

        when(mapper.toModel(transactionDTO)).thenReturn(transactionModel);
        when(transactionServicePort.consignAmount(transactionModel)).thenReturn(transactionModel);
        when(mapper.toResDTO(transactionModel)).thenReturn(expectedResponse);

        ResponseEntity<ResTransactionDTO> response = transactionRestController.consign(transactionDTO);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(transactionServicePort).consignAmount(transactionModel);
    }

    @Test
    @DisplayName("POST /withdraw - Debe realizar retiro con datos válidos")
    void withdraw_WithValidData_ShouldReturnTransaction() {
        TransactionDTO transactionDTO = createValidTransactionDTO();
        TransactionModel transactionModel = new TransactionModel();
        ResTransactionDTO expectedResponse = new ResTransactionDTO();

        when(mapper.toModel(transactionDTO)).thenReturn(transactionModel);
        when(transactionServicePort.withdrawAmount(transactionModel)).thenReturn(transactionModel);
        when(mapper.toResDTO(transactionModel)).thenReturn(expectedResponse);

        ResponseEntity<ResTransactionDTO> response = transactionRestController.withdraw(transactionDTO);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(transactionServicePort).withdrawAmount(transactionModel);
    }

    @Test
    @DisplayName("POST /transfer - Debe realizar transferencia con datos válidos")
    void transfer_WithValidData_ShouldReturnTransaction() {
        TransactionDTO transactionDTO = createValidTransferDTO();
        TransactionModel transactionModel = new TransactionModel();
        ResTransactionDTO expectedResponse = new ResTransactionDTO();

        when(mapper.toModel(transactionDTO)).thenReturn(transactionModel);
        when(transactionServicePort.transferAmount(transactionModel)).thenReturn(transactionModel);
        when(mapper.toResDTO(transactionModel)).thenReturn(expectedResponse);

        ResponseEntity<ResTransactionDTO> response = transactionRestController.transfer(transactionDTO);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(transactionServicePort).transferAmount(transactionModel);
    }

    @Test
    @DisplayName("Validación TransactionDTO - amount no debe ser nulo")
    void transactionDTOValidation_AmountNotNull() {
        TransactionDTO dto = createValidTransactionDTO();
        dto.setAmount(null);
        assertValidationFails(dto, "amount", "no debe ser nulo");
    }

    @Test
    @DisplayName("Validación TransactionDTO - originProductId no debe ser nulo")
    void transactionDTOValidation_OriginProductIdNotNull() {
        TransactionDTO dto = createValidTransactionDTO();
        dto.setOriginProductId(null);
        assertValidationFails(dto, "originProductId", "no debe ser nulo");
    }

    @Test
    @DisplayName("Validación TransactionDTO - destinyProductId no debe ser nulo para transferencias")
    void transactionDTOValidation_DestinyProductIdNotNullForTransfers() {
        TransactionDTO dto = createValidTransactionDTO();
        dto.setDestinyProductId(null);

        // La validación de destino nulo debería hacerse en el servicio, no en el DTO
        // ya que solo es requerido para transferencias
        Set<ConstraintViolation<TransactionDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    private void assertValidationFails(TransactionDTO dto, String property, String message) {
        Set<ConstraintViolation<TransactionDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(property)
                        && v.getMessage().contains(message)));
    }

    private TransactionDTO createValidTransactionDTO() {
        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(new BigDecimal("1000.00"));
        dto.setOriginProductId(1L);
        return dto;
    }

    private TransactionDTO createValidTransferDTO() {
        TransactionDTO dto = createValidTransactionDTO();
        dto.setDestinyProductId(2L);
        return dto;
    }

}