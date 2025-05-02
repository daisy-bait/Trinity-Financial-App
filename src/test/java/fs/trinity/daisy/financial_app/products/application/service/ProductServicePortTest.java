package fs.trinity.daisy.financial_app.products.application.service;

import static org.junit.jupiter.api.Assertions.*;

import fs.trinity.daisy.financial_app.clients.domain.models.ClientModel;
import fs.trinity.daisy.financial_app.clients.domain.models.IdTypes;
import fs.trinity.daisy.financial_app.clients.domain.ports.input.ClientUseCases;
import fs.trinity.daisy.financial_app.products.domain.models.AccountState;
import fs.trinity.daisy.financial_app.products.domain.models.AccountTypes;
import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.domain.ports.output.ProductRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServicePortTest {

    @Mock
    private ProductRepositoryPort productRepo;

    @Mock
    private ClientUseCases clientServicePort;

    @InjectMocks
    private ProductServicePort productService;

    @Test
    @DisplayName("getProducts - Debe retornar lista de productos")
    void getProducts_ShouldReturnProductList() {

        List<ProductModel> expectedProducts = List.of(
                createTestProduct(1L, AccountTypes.AHORROS, AccountState.ACTIVE),
                createTestProduct(2L, AccountTypes.CORRIENTE, AccountState.INACTIVE)
        );

        when(productRepo.findAll()).thenReturn(expectedProducts);

        List<ProductModel> result = productService.getProducts();

        assertEquals(2, result.size());
        assertEquals(expectedProducts, result);
        verify(productRepo).findAll();
    }

    @Test
    @DisplayName("getProduct - Debe retornar producto cuando existe")
    void getProduct_WithExistingId_ShouldReturnProduct() {

        Long productId = 1L;
        ProductModel expectedProduct = createTestProduct(productId, AccountTypes.AHORROS, AccountState.ACTIVE);

        when(productRepo.findProductById(productId)).thenReturn(Optional.of(expectedProduct));

        ProductModel result = productService.getProduct(productId);

        assertEquals(expectedProduct, result);
        verify(productRepo).findProductById(productId);
    }

    @Test
    @DisplayName("getProduct - Debe lanzar excepción cuando producto no existe")
    void getProduct_WithNonExistingId_ShouldThrowException() {

        Long productId = 99L;

        when(productRepo.findProductById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            productService.getProduct(productId);
        });
        verify(productRepo).findProductById(productId);
    }

    @Test
    @DisplayName("createProduct - Debe crear cuenta de ahorros activa por defecto")
    void createProduct_SavingsAccount_ShouldBeActiveByDefault() {

        ProductModel newProduct = createTestProduct(null, AccountTypes.CORRIENTE, null);
        ClientModel testClient = createTestClient(1L);
        newProduct.setClient(testClient);

        when(clientServicePort.getClient(anyLong())).thenReturn(testClient);
        when(productRepo.saveProduct(any(ProductModel.class))).thenAnswer(invocation -> {
            ProductModel savedProduct = invocation.getArgument(0);
            savedProduct.setId(1L); // Simular ID generado
            savedProduct.setProductNumber("33" + "12345678"); // Simular número generado
            savedProduct.setCreatedDate(LocalDateTime.now());
            savedProduct.setLastModifiedDate(LocalDateTime.now());
            savedProduct.setClient(testClient); // Mantener la referencia al cliente
            return savedProduct;
        });

        ProductModel result = productService.createProduct(newProduct);

        assertNotNull(result.getId());
        assertEquals(AccountState.INACTIVE, result.getProductState());
        assertNotNull(result.getProductNumber());
        assertTrue(result.getProductNumber().startsWith("33"));
        assertEquals(10, result.getProductNumber().length());
        assertNotNull(result.getClient());
        assertEquals(testClient.getId(), result.getClient().getId());
        verify(productRepo).saveProduct(any(ProductModel.class));

    }

    @Test
    @DisplayName("createProduct - Debe crear cuenta corriente inactiva por defecto")
    void createProduct_CheckingAccount_ShouldBeInactiveByDefault() {

        ProductModel newProduct = createTestProduct(null, AccountTypes.CORRIENTE, null);
        ClientModel testClient = createTestClient(1L);
        newProduct.setClient(testClient);

        when(clientServicePort.getClient(anyLong())).thenReturn(testClient);
        when(productRepo.saveProduct(any(ProductModel.class))).thenAnswer(invocation -> {
            ProductModel productToSave = invocation.getArgument(0);

            ProductModel savedProduct = new ProductModel();
            savedProduct.setId(1L);
            savedProduct.setProductType(productToSave.getProductType());
            savedProduct.setProductState(AccountState.INACTIVE); // Estado por defecto para corriente
            savedProduct.setProductNumber("33" + "12345678"); // Número generado
            savedProduct.setBalance(BigDecimal.ZERO);
            savedProduct.setGmfExempt(false);
            savedProduct.setCreatedDate(LocalDateTime.now());
            savedProduct.setLastModifiedDate(LocalDateTime.now());
            savedProduct.setClient(testClient);

            return savedProduct;
        });

        ProductModel result = productService.createProduct(newProduct);

        assertNotNull(result.getId());
        assertEquals(AccountState.INACTIVE, result.getProductState());
        assertNotNull(result.getProductNumber());
        assertTrue( result.getProductNumber().startsWith("33"));
        assertEquals(10, result.getProductNumber().length());
        assertNotNull(result.getClient());
        assertEquals(testClient.getId(), result.getClient().getId());
        verify(productRepo).saveProduct(any(ProductModel.class));
    }

    @Test
    @DisplayName("activeProduct - Debe activar producto inactivo")
    void activeProduct_WithInactiveProduct_ShouldActivate() {

        Long productId = 1L;
        ProductModel inactiveProduct = createTestProduct(productId, AccountTypes.CORRIENTE, AccountState.INACTIVE);

        when(productRepo.findProductById(productId)).thenReturn(Optional.of(inactiveProduct));
        when(productRepo.saveProduct(any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductModel result = productService.activeProduct(productId);

        assertEquals(AccountState.ACTIVE, result.getProductState());
        assertNotNull(result.getLastModifiedDate());
        verify(productRepo).saveProduct(inactiveProduct);
    }

    @Test
    @DisplayName("activeProduct - Debe lanzar excepción si producto ya está activo")
    void activeProduct_WithActiveProduct_ShouldThrowException() {

        Long productId = 1L;
        ProductModel activeProduct = createTestProduct(productId, AccountTypes.AHORROS, AccountState.ACTIVE);

        when(productRepo.findProductById(productId)).thenReturn(Optional.of(activeProduct));

        assertThrows(RuntimeException.class, () -> {
            productService.activeProduct(productId);
        });
        verify(productRepo, never()).saveProduct(any());
    }

    @Test
    @DisplayName("disableProduct - Debe desactivar producto activo")
    void disableProduct_WithActiveProduct_ShouldDeactivate() {

        Long productId = 1L;
        ProductModel activeProduct = createTestProduct(productId, AccountTypes.AHORROS, AccountState.ACTIVE);

        when(productRepo.findProductById(productId)).thenReturn(Optional.of(activeProduct));
        when(productRepo.saveProduct(any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductModel result = productService.disableProduct(productId);

        assertEquals(AccountState.INACTIVE, result.getProductState());
        assertNotNull(result.getLastModifiedDate());
        verify(productRepo).saveProduct(activeProduct);
    }

    @Test
    @DisplayName("cancelProduct - Debe cancelar producto con saldo cero")
    void cancelProduct_WithZeroBalance_ShouldCancel() {

        Long productId = 1L;
        ProductModel product = createTestProduct(productId, AccountTypes.AHORROS, AccountState.ACTIVE);
        product.setBalance(BigDecimal.ZERO);

        when(productRepo.findProductById(productId)).thenReturn(Optional.of(product));
        when(productRepo.saveProduct(any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductModel result = productService.cancelProduct(productId);

        assertEquals(AccountState.CANCELLED, result.getProductState());
        assertNotNull(result.getLastModifiedDate());
        verify(productRepo).saveProduct(product);
    }

    @Test
    @DisplayName("cancelProduct - Debe lanzar excepción si saldo no es cero")
    void cancelProduct_WithNonZeroBalance_ShouldThrowException() {

        Long productId = 1L;
        ProductModel product = createTestProduct(productId, AccountTypes.AHORROS, AccountState.ACTIVE);
        product.setBalance(new BigDecimal("1000"));

        when(productRepo.findProductById(productId)).thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class, () -> {
            productService.cancelProduct(productId);
        });
        verify(productRepo, never()).saveProduct(any());
    }

    @Test
    @DisplayName("deleteProduct - Debe eliminar producto cancelado")
    void deleteProduct_WithCancelledProduct_ShouldDelete() {

        Long productId = 1L;
        ProductModel cancelledProduct = createTestProduct(productId, AccountTypes.AHORROS, AccountState.CANCELLED);

        when(productRepo.findProductById(productId)).thenReturn(Optional.of(cancelledProduct));
        doNothing().when(productRepo).deleteProductById(productId);

        boolean result = productService.deleteProduct(productId);

        assertTrue(result);
        verify(productRepo).deleteProductById(productId);
    }

    @Test
    @DisplayName("deleteProduct - Debe lanzar excepción si producto no está cancelado")
    void deleteProduct_WithNonCancelledProduct_ShouldThrowException() {

        Long productId = 1L;
        ProductModel activeProduct = createTestProduct(productId, AccountTypes.AHORROS, AccountState.ACTIVE);

        when(productRepo.findProductById(productId)).thenReturn(Optional.of(activeProduct));

        assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(productId);
        });
        verify(productRepo, never()).deleteProductById(any());
    }

    private ProductModel createTestProduct(Long id, AccountTypes type, AccountState state) {
        ProductModel product = new ProductModel();
        product.setId(id);
        product.setProductType(type);
        product.setProductState(state);
        product.setBalance(BigDecimal.ZERO);
        product.setGmfExempt(false);
        product.setCreatedDate(LocalDateTime.now());
        product.setLastModifiedDate(LocalDateTime.now());
        return product;
    }

    private ClientModel createTestClient(Long id) {
        ClientModel client = new ClientModel();
        client.setId(id);
        client.setIdType(IdTypes.CIUDADANIA_CEDULA);
        client.setIdNum("1108452608");
        client.setFirstName("Nicolle");
        client.setLastName("Martínez");
        client.setEmail("nicolle_army@gmail.com");
        client.setBirthDate(LocalDate.of(1990, 1, 1));
        return client;
    }
}
