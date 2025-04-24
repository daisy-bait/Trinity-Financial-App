package fs.trinity.daisy.financial_app.clients.infrastructure.input.rest.controller;

import fs.trinity.daisy.financial_app.clients.application.service.ClientServicePort;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/client")
public class ClientRestController {

    private final ClientServicePort clientServicePort;

}
