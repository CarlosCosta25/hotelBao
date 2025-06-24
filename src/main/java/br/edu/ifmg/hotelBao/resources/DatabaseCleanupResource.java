package br.edu.ifmg.hotelBao.resources;

import br.edu.ifmg.hotelBao.service.DatabaseCleanupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/admin/database")
@Tag(name = "Database Cleanup", description = "Controller for database maintenance operations")

public class DatabaseCleanupResource {

    @Autowired
    private DatabaseCleanupService cleanupService;

    @Operation(
            description = "Limpar todo o banco de dados",
            summary = "Deleta todos os dados do banco de dados (Irreversível)",
            responses = {
                    @ApiResponse(description = "204 No Content – cleaned", responseCode = "204"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500")
            }
    )
    @DeleteMapping("/clean-all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> cleanAllData() {
        cleanupService.cleanAllDataDatabase();
        return ResponseEntity.noContent().build();
    }

}