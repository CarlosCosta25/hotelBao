package br.edu.ifmg.hotelBao.resources;

import br.edu.ifmg.hotelBao.dto.InvoiceDTO;
import br.edu.ifmg.hotelBao.service.InvoiceService;
import br.edu.ifmg.hotelBao.service.exceptions.ResourceNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
@Tag(name = "Invoice", description = "Controller/Resource for invoice generation")
public class InvoiceResource {

    @Autowired
    private InvoiceService invoiceService;

    @Operation(
            summary = "Generate invoice for a client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Client Not Found"),
                    @ApiResponse(responseCode = "400", description = "Bad Request: no stays or incomplete data")
            }
    )
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLIENT')")
    public ResponseEntity<InvoiceDTO> generateInvoice(@PathVariable Long clientId) {
        try {
            InvoiceDTO invoice = invoiceService.generateInvoice(clientId);
            return ResponseEntity.ok(invoice);
        } catch (ResourceNotFound ex) {
            return ResponseEntity.status(404).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}