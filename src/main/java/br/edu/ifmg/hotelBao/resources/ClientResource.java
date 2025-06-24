package br.edu.ifmg.hotelBao.resources;

import br.edu.ifmg.hotelBao.dto.ClientDTO;
import br.edu.ifmg.hotelBao.dto.ClientInsertDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import br.edu.ifmg.hotelBao.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/user")
@Tag(name = "Client", description = "Controller/Resource for clients")
public class ClientResource {

    @Autowired
    ClientService clientService;


    @Operation(
            description ="Get all client ",
            summary = "Get all client (ROLE_CLIENT OR ROLE_ADMIN)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<ClientDTO>> getAll(Pageable pageable) {
        // Page<ClientDTO> clients = clientService.findAll(pageable);
        return ResponseEntity.ok(clientService.findAll(pageable));
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description ="Get a client",
            summary = "Get a client (ROLE_CLIENT OR ROLE_ADMIN)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ClientDTO>getById(@PathVariable long id){
        return ResponseEntity.ok(clientService.findById(id));
    }


    @PostMapping( produces = "application/json")
    @Operation(
            description ="Create a new client",
            summary = "Create a new client",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbbiden", responseCode = "403"),
                    @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
            }
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ClientDTO>insert(@Valid @RequestBody ClientInsertDTO dto){
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(clientService.insert(dto));
    }


    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description ="Update a client",
            summary = "Update a client",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbbiden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
            }
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ClientDTO>update( @PathVariable Long id,@Valid @RequestBody ClientDTO dto){
        return ResponseEntity.ok(clientService.update(id,dto));
    }


    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description ="Delete a client",
            summary = "Delete a client",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbbiden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
            }
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ClientDTO>delete(@PathVariable long id){
        return ResponseEntity.ok(clientService.delete(id));
    }


    @PostMapping(value = "/signup", produces = "application/json")
    @Operation(
            description = "Sign up a client",
            summary = "Client self-register",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400")
            }
    )
    public ResponseEntity<ClientDTO> signUp(@Valid @RequestBody ClientInsertDTO dto) {
        ClientDTO client = clientService.signUp(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getId())
                .toUri();
        return ResponseEntity.created(uri).body(client);
    }


    @Operation(
            description = "Get all clients",
            summary = "Get all clients (ROLE_CLIENT)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping(value = "/clients", produces = "application/json")
    public ResponseEntity<Page<ClientDTO>> getAllClients(Pageable pageable) {
        return ResponseEntity.ok(clientService.findAllClients(pageable));
    }


    @Operation(
            description = "Get a client by ID",
            summary = "Get a client by ID (ROLE_CLIENT)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping(value = "/clients/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findClientById(id));
    }


}
