package fs.trinity.daisy.financial_app.products.infrastructure.input.rest.controller;

import fs.trinity.daisy.financial_app.products.domain.models.ProductModel;
import fs.trinity.daisy.financial_app.products.domain.ports.input.ProductUseCases;
import fs.trinity.daisy.financial_app.products.infrastructure.input.rest.dto.ProductDTO;
import fs.trinity.daisy.financial_app.products.infrastructure.input.rest.dto.ResProductDTO;
import fs.trinity.daisy.financial_app.products.infrastructure.input.rest.mapper.ProductRestMapper;
import fs.trinity.daisy.financial_app.shared.infrastructure.model.PageResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductRestController {

    private final ProductUseCases productServicePort;

    private final ProductRestMapper mapper;

    @GetMapping("/find-all")
    ResponseEntity<List<ResProductDTO>> retrieveProducts() {
        return ResponseEntity.ok(productServicePort.getProducts()
                .stream().map(mapper::toResDTO).toList());
    }

    @GetMapping("/find/{id}")
    ResponseEntity<ResProductDTO> retrieveProductById(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(mapper.toResDTO(productServicePort.getProduct(productId)));
    }

    @GetMapping("/find-by-client/{id}")
    ResponseEntity<List<ResProductDTO>> retrieveProductByClientId(@PathVariable("id") Long clientId) {
        return ResponseEntity.ok(productServicePort.getProductsByClientId(clientId)
                .stream().map(mapper::toResDTO).toList());
    }

    @GetMapping("/find-by-number")
    ResponseEntity<PageResponse<ResProductDTO>> retrieveProductByNumber(
            @RequestParam String productNumber,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "3") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ProductModel> pageResponse = productServicePort.pageProductsByProductNumber(pageable, productNumber);
        PageResponse<ResProductDTO> dtoPageResponse =  new PageResponse<>();
        dtoPageResponse.setContent(pageResponse.getContent().stream().map(mapper::toResDTO).toList());
        dtoPageResponse.setTotalElements(pageResponse.getTotalElements());
        dtoPageResponse.setTotalPages(pageResponse.getTotalPages());
        dtoPageResponse.setPageNumber(pageResponse.getPageNumber());
        dtoPageResponse.setPageSize(pageResponse.getPageSize());
        return ResponseEntity.ok(dtoPageResponse);
    }

    @PostMapping("/save")
    ResponseEntity<ResProductDTO> saveProduct(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(mapper.toResDTO(productServicePort.createProduct(mapper.toModel(productDTO))));
    }

    @PutMapping("/active/{id}")
    ResponseEntity<ResProductDTO> activeProduct(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(mapper.toResDTO(productServicePort.activeProduct(productId)));
    }

    @PutMapping("/disable/{id}")
    ResponseEntity<ResProductDTO> disableProduct(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(mapper.toResDTO(productServicePort.disableProduct(productId)));
    }

    @PutMapping("/cancel/{id}")
    ResponseEntity<ResProductDTO> cancelProduct(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(mapper.toResDTO(productServicePort.cancelProduct(productId)));
    }

    @PutMapping("/gmf-exempt/{id}")
    ResponseEntity<ResProductDTO> exemptGMF(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(mapper.toResDTO(productServicePort.exemptGMF(productId)));
    }

    @PutMapping("/disable-gmf-exempt/{id}")
    ResponseEntity<ResProductDTO> disableExemptGMF(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(mapper.toResDTO(productServicePort.disableExemptGMF(productId)));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Boolean> deleteProduct(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productServicePort.deleteProduct(productId));
    }

}