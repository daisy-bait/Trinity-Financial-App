package fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.controller;

import fs.trinity.daisy.financial_app.clients.domain.ports.input.ClientUseCases;
import fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.dto.ClientDTO;
import fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.dto.ResClientDTO;
import fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.mapper.ClientRestMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/client")
public class ClientRestController {

    private final ClientUseCases clientServicePort;

    private final ClientRestMapper mapper;

    @GetMapping("/find-all")
    public ResponseEntity<List<ResClientDTO>> retrieveClients() {
        return ResponseEntity.ok(clientServicePort.getClientsModels()
                .stream().map(mapper::toResDTO).toList());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ResClientDTO> retrieveClientById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResDTO(clientServicePort.getClientModel(id)));
    }

    @PostMapping("/save")
    public ResponseEntity<ResClientDTO> saveClient(@Valid @RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(mapper.toResDTO(clientServicePort.createClient(mapper.toModel(clientDTO))));
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<ResClientDTO> updateClient(
            @Valid @RequestBody ClientDTO clientDTO,
            @PathVariable("id") Long clientId) {
        return ResponseEntity.ok(mapper.toResDTO(
                clientServicePort.modifyClient(mapper.toModel(clientDTO), clientId))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteClient(@PathVariable("id") Long clientId) {
        return ResponseEntity.ok(clientServicePort.deleteClient(clientId));
    }

}