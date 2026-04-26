package fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.controller;

import fs.trinity.daisy.financial_app.shared.infrastructure.model.PageResponse;
import fs.trinity.daisy.financial_app.transactions.application.service.TransactionServicePort;
import fs.trinity.daisy.financial_app.transactions.domain.models.TransactionModel;
import fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.dto.ResTransactionDTO;
import fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.dto.TransactionDTO;
import fs.trinity.daisy.financial_app.transactions.infrastructure.input.rest.mapper.TransactionRestMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transaction")
public class TransactionRestController {

    private final TransactionServicePort transactionServicePort;

    private final TransactionRestMapper mapper;

    @GetMapping("/find-all")
    ResponseEntity<List<ResTransactionDTO>> findAll() {
        return ResponseEntity.ok(transactionServicePort.getAllTransactions()
                .stream().map(mapper::toResDTO).toList());
    }

    @GetMapping("/find-page")
    ResponseEntity<PageResponse<ResTransactionDTO>> findPage(
            @RequestParam String productNumber,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "3") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<TransactionModel> pageResponse = transactionServicePort.pageTransactions(pageable, productNumber);
        PageResponse<ResTransactionDTO> dtoPageResponse = new PageResponse<>();
        dtoPageResponse.setContent(pageResponse.getContent().stream().map(mapper::toResDTO).toList());
        dtoPageResponse.setPageNumber(pageResponse.getPageNumber());
        dtoPageResponse.setPageSize(pageResponse.getPageSize());
        dtoPageResponse.setTotalElements(pageResponse.getTotalElements());
        dtoPageResponse.setTotalPages(pageResponse.getTotalPages());
        return ResponseEntity.ok(
                dtoPageResponse
        );
    }

    @GetMapping("/find/{id}")
    ResponseEntity<ResTransactionDTO> findById(@PathVariable("id") Long transactionId) {
        return ResponseEntity.ok(mapper.toResDTO(transactionServicePort.getTransactionById(transactionId)));
    }

    @PostMapping("/consign")
    ResponseEntity<ResTransactionDTO> consign(@RequestBody TransactionDTO transactionDTO) {
        TransactionModel transactionModel = mapper.toModel(transactionDTO);
        return ResponseEntity.ok(mapper.toResDTO(transactionServicePort.consignAmount(transactionModel)));
    }

    @PostMapping("/withdraw")
    ResponseEntity<ResTransactionDTO> withdraw(@RequestBody TransactionDTO transactionDTO) {
        TransactionModel transactionModel = mapper.toModel(transactionDTO);
        return ResponseEntity.ok(mapper.toResDTO(transactionServicePort.withdrawAmount(transactionModel)));
    }

    @PostMapping("/transfer")
    ResponseEntity<ResTransactionDTO> transfer(@RequestBody TransactionDTO transactionDTO) {
        TransactionModel transactionModel = mapper.toModel(transactionDTO);
        return ResponseEntity.ok(mapper.toResDTO(transactionServicePort.transferAmount(transactionModel)));
    }

}