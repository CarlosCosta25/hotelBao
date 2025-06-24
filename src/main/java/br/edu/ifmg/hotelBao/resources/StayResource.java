package br.edu.ifmg.hotelBao.resources;

import br.edu.ifmg.hotelBao.dto.StayDTO;
import br.edu.ifmg.hotelBao.service.StayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stay")
@Tag(name = "Stay", description = "Controller/Resource for stays")
public class StayResource {

    @Autowired private StayService stayService;

    @Operation(summary = "Get all stays", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(produces = "application/json")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<StayDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(stayService.findAll(pageable));
    }



    @Operation(summary = "Get a stay", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<StayDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stayService.findById(id));
    }


    @Operation(summary = "Create a new stay", responses = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity")
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<StayDTO> insert(@Valid @RequestBody StayDTO dto) {
        StayDTO result = stayService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @Operation(summary = "Update a stay", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity")
    })
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<StayDTO> update(@PathVariable Long id, @Valid @RequestBody StayDTO dto) {
        return ResponseEntity.ok(stayService.update(id, dto));
    }

    @Operation(summary = "Delete a stay", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<StayDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok(stayService.delete(id));
    }

    @Operation(summary = "Get all stays of a client", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Client Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value = "/client/{clientId}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLIENT')")
    public ResponseEntity<List<StayDTO>> getByClient(@PathVariable Long clientId) {
        List<StayDTO> list = stayService.findByClientId(clientId);
        return ResponseEntity.ok(list);
    }

}