package fs.trinity.daisy.financial_app.products.infrastructure.input.rest.controller;

import fs.trinity.daisy.financial_app.products.domain.models.AccountState;
import fs.trinity.daisy.financial_app.products.domain.models.AccountTypes;
import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.domain.ports.input.ProductUseCases;
import fs.trinity.daisy.financial_app.products.infrastructure.input.rest.dto.ProductDTO;
import fs.trinity.daisy.financial_app.products.infrastructure.input.rest.dto.ResProductDTO;
import fs.trinity.daisy.financial_app.products.infrastructure.input.rest.mapper.ProductRestMapper;
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

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductRestControllerTest {

    @Mock
    private ProductUseCases productServicePort;

    @Mock
    private ProductRestMapper productRestMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private ProductRestController productRestController;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.afterPropertiesSet();
        validator = factory;
    }

    @Test
    @DisplayName("GET /find-all - Debe retornar lista de productos mapeados")
    void retrieveProducts_ShouldReturnListOfProducts() {
        List<ProductModel> products = List.of(new ProductModel(), new ProductModel());
        List<ResProductDTO> expectedDtos = List.of(new ResProductDTO(), new ResProductDTO());

        when(productServicePort.getProducts()).thenReturn(products);
        when(productRestMapper.toResDTO(any(ProductModel.class))).thenReturn(expectedDtos.get(0), expectedDtos.get(1));

        ResponseEntity<List<ResProductDTO>> response = productRestController.retrieveProducts();

        assertEquals(expectedDtos, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(productServicePort).getProducts();
        verify(productRestMapper, times(2)).toResDTO(any(ProductModel.class));
    }

    @Test
    @DisplayName("GET /find/{id} - Debe retornar producto cuando ID existe")
    void retrieveProductById_WithValidId_ShouldReturnProduct() {
        Long productId = 1L;
        ProductModel product = new ProductModel();
        ResProductDTO expectedDto = new ResProductDTO();

        when(productServicePort.getProduct(productId)).thenReturn(product);
        when(productRestMapper.toResDTO(product)).thenReturn(expectedDto);

        ResponseEntity<ResProductDTO> response = productRestController.retrieveProductById(productId);

        assertEquals(expectedDto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(productServicePort).getProduct(productId);
        verify(productRestMapper).toResDTO(product);
    }

    @Test
    @DisplayName("POST /save - Debe retornar producto creado con datos válidos")
    void saveProduct_WithValidProductDTO_ShouldReturnCreated() {
        ProductDTO validProductDTO = createValidProductDTO();
        ProductModel validProduct = new ProductModel();
        ResProductDTO expectedResponse = new ResProductDTO();

        when(productRestMapper.toModel(validProductDTO)).thenReturn(validProduct);
        when(productServicePort.createProduct(validProduct)).thenReturn(validProduct);
        when(productRestMapper.toResDTO(validProduct)).thenReturn(expectedResponse);

        ResponseEntity<ResProductDTO> response = productRestController.saveProduct(validProductDTO);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("POST /save - Debe validar campos requeridos")
    void saveProduct_WithInvalidFields_ShouldThrowValidationException() {
        ProductDTO invalidProductDTO = new ProductDTO();

        // Test para clientId nulo
        invalidProductDTO.setClientId(null);
        assertValidationFails(invalidProductDTO, "clientId", "no debe ser nulo");

        // Test para productType nulo
        invalidProductDTO.setProductType(null);
        assertValidationFails(invalidProductDTO, "productType", "no debe ser nulo");
    }

    @Test
    @DisplayName("PUT /active/{id} - Debe activar producto existente")
    void activeProduct_WithValidId_ShouldReturnActivatedProduct() {
        Long productId = 1L;
        ProductModel activatedProduct = new ProductModel();
        ResProductDTO expectedDto = new ResProductDTO();

        when(productServicePort.activeProduct(productId)).thenReturn(activatedProduct);
        when(productRestMapper.toResDTO(activatedProduct)).thenReturn(expectedDto);

        ResponseEntity<ResProductDTO> response = productRestController.activeProduct(productId);

        assertEquals(expectedDto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(productServicePort).activeProduct(productId);
    }

    @Test
    @DisplayName("PUT /disable/{id} - Debe desactivar producto existente")
    void disableProduct_WithValidId_ShouldReturnDisabledProduct() {
        Long productId = 1L;
        ProductModel disabledProduct = new ProductModel();
        ResProductDTO expectedDto = new ResProductDTO();

        when(productServicePort.disableProduct(productId)).thenReturn(disabledProduct);
        when(productRestMapper.toResDTO(disabledProduct)).thenReturn(expectedDto);

        ResponseEntity<ResProductDTO> response = productRestController.disableProduct(productId);

        assertEquals(expectedDto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(productServicePort).disableProduct(productId);
    }

    @Test
    @DisplayName("PUT /cancel/{id} - Debe cancelar producto existente")
    void cancelProduct_WithValidId_ShouldReturnCancelledProduct() {
        Long productId = 1L;
        ProductModel cancelledProduct = new ProductModel();
        ResProductDTO expectedDto = new ResProductDTO();

        when(productServicePort.cancelProduct(productId)).thenReturn(cancelledProduct);
        when(productRestMapper.toResDTO(cancelledProduct)).thenReturn(expectedDto);

        ResponseEntity<ResProductDTO> response = productRestController.cancelProduct(productId);

        assertEquals(expectedDto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(productServicePort).cancelProduct(productId);
    }

    @Test
    @DisplayName("PUT /gmf-exempt/{id} - Debe exentar GMF a producto")
    void exemptGMF_WithValidId_ShouldReturnProductWithGMFExempt() {
        Long productId = 1L;
        ProductModel exemptedProduct = new ProductModel();
        ResProductDTO expectedDto = new ResProductDTO();

        when(productServicePort.exemptGMF(productId)).thenReturn(exemptedProduct);
        when(productRestMapper.toResDTO(exemptedProduct)).thenReturn(expectedDto);

        ResponseEntity<ResProductDTO> response = productRestController.exemptGMF(productId);

        assertEquals(expectedDto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(productServicePort).exemptGMF(productId);
    }

    @Test
    @DisplayName("PUT /disable-gmf-exempt/{id} - Debe desactivar exención GMF")
    void disableExemptGMF_WithValidId_ShouldReturnProductWithoutGMFExempt() {
        Long productId = 1L;
        ProductModel nonExemptedProduct = new ProductModel();
        ResProductDTO expectedDto = new ResProductDTO();

        when(productServicePort.disableExemptGMF(productId)).thenReturn(nonExemptedProduct);
        when(productRestMapper.toResDTO(nonExemptedProduct)).thenReturn(expectedDto);

        ResponseEntity<ResProductDTO> response = productRestController.disableExemptGMF(productId);

        assertEquals(expectedDto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(productServicePort).disableExemptGMF(productId);
    }

    @Test
    @DisplayName("DELETE /delete/{id} - Debe retornar true cuando elimina producto existente")
    void deleteProduct_WithValidId_ShouldReturnTrue() {
        Long productId = 1L;

        when(productServicePort.deleteProduct(productId)).thenReturn(true);

        ResponseEntity<Boolean> response = productRestController.deleteProduct(productId);

        assertTrue(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(productServicePort).deleteProduct(productId);
    }

    private void assertValidationFails(ProductDTO dto, String property, String message) {
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(property)
                        && v.getMessage().contains(message)));
    }

    private ProductDTO createValidProductDTO() {
        ProductDTO dto = new ProductDTO();
        dto.setClientId(1L);
        dto.setProductType(AccountTypes.CORRIENTE);
        dto.setProductState(AccountState.ACTIVE);
        dto.setGmfExempt(false);
        return dto;
    }

}