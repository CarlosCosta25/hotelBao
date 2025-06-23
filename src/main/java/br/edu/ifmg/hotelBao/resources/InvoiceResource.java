package br.edu.ifmg.hotelBao.resources;

import br.edu.ifmg.hotelBao.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/invoice")
@Tag(name = "Invoice", description = "Controller/Resource for invoice generation")
public class InvoiceResource {

    @Autowired
    private InvoiceService invoiceService;

    @Operation(
            description = "Generate invoice for a client",
            summary = "Generate invoice based on client stays",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            }
    )
    @GetMapping(value = "/client/{clientId}", produces = "text/plain")
    public ResponseEntity<String> generateInvoice(@PathVariable Long clientId) {
        String invoice = invoiceService.generateInvoice(clientId);
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain; charset=utf-8")
                .body(invoice);
    }
}