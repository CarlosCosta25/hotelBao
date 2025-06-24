package br.edu.ifmg.hotelBao.resources;

import br.edu.ifmg.hotelBao.dto.InvoiceDTO;
import br.edu.ifmg.hotelBao.service.InvoiceService;
import br.edu.ifmg.hotelBao.service.exceptions.ResourceNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/invoice")
@Tag(name = "Invoice", description = "Controller/Resource for invoice generation")
public class InvoiceResource {

    @Autowired
    private InvoiceService invoiceService;

    @Operation(
            summary = "Generate structured invoice for a client",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Bad Request", responseCode = "400")
            }
    )
    @GetMapping(value = "/client/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<InvoiceDTO> generateInvoice(@PathVariable Long clientId) {
        try {
            InvoiceDTO dto = invoiceService.generateInvoice(clientId);
            if (!dto.isSuccess()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
            }
            return ResponseEntity.ok(dto);
        } catch (ResourceNotFound ex) {
            InvoiceDTO error = new InvoiceDTO(false, ex.getMessage(), null, null, null, null, 0.0);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
