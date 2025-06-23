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
            description = "Clean all data from database",
            summary = "Delete all records from all tables (DANGER: irreversible operation)",
            responses = {
                    @ApiResponse(description = "OK - Database cleaned successfully", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500")
            }
    )
    @DeleteMapping("/clean-all")
    public ResponseEntity<Map<String, Object>> cleanAllData() {
        Map<String, Object> result = cleanupService.cleanAllData();
        return ResponseEntity.ok(result);
    }





    @Operation(
            description = "Get database statistics",
            summary = "Get count of records in each table",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            }
    )
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getDatabaseStats() {
        Map<String, Long> stats = cleanupService.getDatabaseStats();
        return ResponseEntity.ok(stats);
    }
}