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

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stay")
@Tag(name = "Stay", description = "Controller/Resource for stays")
public class StayResource {

    @Autowired private StayService stayService;

    @Operation(
            description ="Buscar todas estadias",
            summary = "Buscar todas estadias",
            responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @GetMapping(produces = "application/json")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<StayDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(stayService.findAll(pageable));
    }



    @Operation(
            description ="Buscar estadia por ID",
            summary = "Buscar estadia por I", responses = {
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


    @Operation(
            description ="Criar nova estadia",
            summary = "Criar nova estadia",
            responses = {
                @ApiResponse(responseCode = "201", description = "Created"),
                @ApiResponse(responseCode = "400", description = "Bad Request"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "403", description = "Forbidden"),
                @ApiResponse(responseCode = "422", description = "Unprocessable Entity")
            })
    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<StayDTO> insert(@Valid @RequestBody StayDTO dto) {
        StayDTO result = stayService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @Operation(
            description ="Atualizar estadia pelo ID",
            summary = "Atualizar estadia pelo ID",
            responses = {
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

    @Operation(
            description ="Deletar estadia pelo ID",
            summary = "Deletar estadia pelo ID",
            responses = {
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

    @Operation(
            description ="Buscar todas estadia de um determinado cliente pelo ID",
            summary = "Buscar todas estadia de um determinado cliente pelo ID",
            responses = {
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


    @Operation(
            description = "Calcular o valor total das estadias do cliente",
            summary = "Calcular o valor total das estadias do cliente",
            responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(responseCode = "404", description = "Client Not Found"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @GetMapping(value = "/client/{clientId}/total", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLIENT')")
    public ResponseEntity<Map<String, BigDecimal>> getTotalValueByClient(@PathVariable Long clientId) {
        BigDecimal totalValue = stayService.getTotalValueByClient(clientId);
        Map<String, BigDecimal> response = Map.of("total_estadia", totalValue);
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "Buscar estadia com maior valor do cliente pelo ID",
            summary = "Buscar estadia com maior valor do cliente pelo ID",
            responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(responseCode = "404", description = "Client Not Found or No Stays Found"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "403", description = "Forbidden")
        })
    @GetMapping(value = "/client/{clientId}/highest", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLIENT')")
    public ResponseEntity<StayDTO> getHighestValueStayByClient(@PathVariable Long clientId) {
        StayDTO highestValueStay = stayService.getHighestValueStayByClient(clientId);
        return ResponseEntity.ok(highestValueStay);
    }

    @Operation(
            description = "Buscar estadia com maior valor do cliente pelo ID",
            summary = "Buscar estadia com maior valor do cliente pelo ID",
            responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(responseCode = "404", description = "Client Not Found or No Stays Found"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "403", description = "Forbidden")
        })
    @GetMapping(value = "/client/{clientId}/lowest", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLIENT')")
    public ResponseEntity<StayDTO> getLowestValueStayByClient(@PathVariable Long clientId) {
        StayDTO lowestValueStay = stayService.getLowestValueStayByClient(clientId);
        return ResponseEntity.ok(lowestValueStay);
    }

}