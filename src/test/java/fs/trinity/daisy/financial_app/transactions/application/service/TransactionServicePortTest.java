package fs.trinity.daisy.financial_app.transactions.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.products.domain.models.AccountState;
import fs.trinity.daisy.financial_app.products.domain.models.AccountTypes;
import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.domain.ports.input.ProductUseCases;
import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionModel;
import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionTypes;
import fs.trinity.daisy.financial_app.transactions.domain.ports.output.TransactionRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServicePortTest {

    @Mock
    private TransactionRepositoryPort transactionRepo;

    @Mock
    private ProductUseCases productServicePort;

    @InjectMocks
    private TransactionServicePort transactionService;

    @Test
    @DisplayName("getAllTransactions - Debe retornar lista de transacciones")
    void getAllTransactions_ShouldReturnTransactionList() {

        List<TransactionModel> expectedTransactions = List.of(
                createTestTransaction(1L, TransactionTypes.CONSIGNMENT),
                createTestTransaction(2L, TransactionTypes.WITHDRAWAL)
        );

        when(transactionRepo.findAllTransactions()).thenReturn(expectedTransactions);

        List<TransactionModel> result = transactionService.getAllTransactions();

        assertEquals(2, result.size());
        assertEquals(expectedTransactions, result);
        verify(transactionRepo).findAllTransactions();
    }

    @Test
    @DisplayName("getTransactionById - Debe retornar transacción cuando existe")
    void getTransactionById_WithExistingId_ShouldReturnTransaction() {

        Long transactionId = 1L;
        TransactionModel expectedTransaction = createTestTransaction(transactionId, TransactionTypes.CONSIGNMENT);

        when(transactionRepo.findTransactionById(transactionId)).thenReturn(Optional.of(expectedTransaction));

        TransactionModel result = transactionService.getTransactionById(transactionId);

        assertEquals(expectedTransaction, result);
        verify(transactionRepo).findTransactionById(transactionId);
    }

    @Test
    @DisplayName("consignAmount - Debe realizar consignación exitosa")
    void consignAmount_WithValidData_ShouldReturnTransaction() {

        ProductModel product = createTestProduct(1L, AccountTypes.AHORROS, AccountState.ACTIVE);
        product.setBalance(BigDecimal.ZERO);

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setAmount(new BigDecimal("1000"));
        transactionModel.setOriginProduct(product);

        TransactionModel expectedTransaction = createTestTransaction(1L, TransactionTypes.CONSIGNMENT);

        when(productServicePort.getProduct(anyLong())).thenReturn(product);
        when(transactionRepo.saveTransaction(any(TransactionModel.class))).thenReturn(expectedTransaction);

        TransactionModel result = transactionService.consignAmount(transactionModel);

        assertEquals(TransactionTypes.CONSIGNMENT, result.getTransactionType());
        assertEquals(new BigDecimal("1000"), product.getBalance());
        verify(productServicePort).updateProduct(product);
        verify(transactionRepo).saveTransaction(any(TransactionModel.class));
    }

    @Test
    @DisplayName("withdrawAmount - Debe realizar retiro exitoso sin GMF")
    void withdrawAmount_WithValidDataNoGMF_ShouldReturnTransaction() {

        ProductModel product = createTestProduct(1L, AccountTypes.AHORROS, AccountState.ACTIVE);
        product.setBalance(new BigDecimal("2000"));
        product.setGmfExempt(true);

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setAmount(new BigDecimal("1000"));
        transactionModel.setOriginProduct(product);

        TransactionModel expectedTransaction = createTestTransaction(1L, TransactionTypes.WITHDRAWAL);

        when(productServicePort.getProduct(anyLong())).thenReturn(product);
        when(transactionRepo.saveTransaction(any(TransactionModel.class))).thenReturn(expectedTransaction);

        TransactionModel result = transactionService.withdrawAmount(transactionModel);

        assertEquals(TransactionTypes.WITHDRAWAL, result.getTransactionType());
        assertEquals(new BigDecimal("1000"), product.getBalance());
        verify(productServicePort).updateProduct(product);
    }

    @Test
    @DisplayName("withdrawAmount - Debe aplicar GMF cuando producto no está exento")
    void withdrawAmount_WithGMF_ShouldApplyTax() {

        ProductModel product = createTestProduct(1L, AccountTypes.AHORROS, AccountState.ACTIVE);
        product.setBalance(new BigDecimal("2000"));
        product.setGmfExempt(false);

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setAmount(new BigDecimal("1000"));
        transactionModel.setOriginProduct(product);

        when(productServicePort.getProduct(anyLong())).thenReturn(product);
        when(transactionRepo.saveTransaction(any(TransactionModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionModel result = transactionService.withdrawAmount(transactionModel);

        assertThat(result.getAmount()).isEqualByComparingTo("1004.00");
        assertThat(product.getBalance()).isEqualByComparingTo("996.00");
    }

    @Test
    @DisplayName("transferAmount - Debe realizar transferencia exitosa entre cuentas")
    void transferAmount_WithValidData_ShouldReturnTransaction() {

        ProductModel originProduct = createTestProduct(1L, AccountTypes.AHORROS, AccountState.ACTIVE);
        originProduct.setBalance(new BigDecimal("2000"));
        originProduct.setGmfExempt(true);

        ProductModel destinyProduct = createTestProduct(2L, AccountTypes.AHORROS, AccountState.ACTIVE);
        destinyProduct.setBalance(BigDecimal.ZERO);

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setAmount(new BigDecimal("1000"));
        transactionModel.setOriginProduct(originProduct);
        transactionModel.setDestinyProduct(destinyProduct);

        when(productServicePort.getProduct(1L)).thenReturn(originProduct);
        when(productServicePort.getProduct(2L)).thenReturn(destinyProduct);
        when(transactionRepo.saveTransaction(any(TransactionModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionModel result = transactionService.transferAmount(transactionModel);

        assertEquals(TransactionTypes.TRANSFERENCE, result.getTransactionType());
        assertEquals(new BigDecimal("1000"), result.getAmount());
        assertEquals(new BigDecimal("1000"), originProduct.getBalance());
        assertEquals(new BigDecimal("1000"), destinyProduct.getBalance());
        verify(productServicePort, times(2)).updateProduct(any(ProductModel.class));
    }

    @Test
    @DisplayName("deleteTransaction - Debe eliminar transacción existente")
    void deleteTransaction_WithExistingId_ShouldReturnTrue() {

        Long transactionId = 1L;
        TransactionModel existingTransaction = createTestTransaction(transactionId, TransactionTypes.CONSIGNMENT);

        when(transactionRepo.findTransactionById(transactionId)).thenReturn(Optional.of(existingTransaction));
        doNothing().when(transactionRepo).deleteTransactionById(transactionId);

        boolean result = transactionService.deleteTransaction(transactionId);

        assertTrue(result);
        verify(transactionRepo).deleteTransactionById(transactionId);
    }

    private TransactionModel createTestTransaction(Long id, TransactionTypes type) {
        TransactionModel transaction = new TransactionModel();
        transaction.setId(id);
        transaction.setTransactionType(type);
        transaction.setAmount(new BigDecimal("1000"));
        transaction.setTransactionDate(LocalDateTime.now());
        return transaction;
    }

    private ProductModel createTestProduct(Long id, AccountTypes type, AccountState state) {
        ProductModel product = new ProductModel();
        product.setId(id);
        product.setProductType(type);
        product.setProductState(state);
        product.setBalance(BigDecimal.ZERO);
        product.setClient(createTestClient(1L));
        return product;
    }

    private ClientModel createTestClient(Long id) {
        ClientModel client = new ClientModel();
        client.setId(id);
        client.setFirstName("Kaleth");
        client.setLastName("Narváez");
        return client;
    }
}