package fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.controller;

import fs.trinity.daisy.financial_app.clients.application.service.ClientServicePort;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/client")
public class ClientRestController {

    private final ClientServicePort clientServicePort;

    @GetMapping("/find-all")
    public ResponseEntity<?> retrieveClients() {
        return ResponseEntity.ok(clientServicePort.getClientsModels());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> retrieveClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientServicePort.getClientModel(id));
    }

}
