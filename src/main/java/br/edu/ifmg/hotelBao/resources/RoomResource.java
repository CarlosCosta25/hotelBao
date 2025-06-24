package br.edu.ifmg.hotelBao.resources;

import br.edu.ifmg.hotelBao.dto.RoomDTO;
import br.edu.ifmg.hotelBao.service.RoomService;
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

@RestController
@RequestMapping(value = "/room")

@Tag(name = "Room", description = "Controller/Resource for rooms")
public class RoomResource {

    @Autowired
    RoomService roomService;

    @Operation(
            description ="Buscar todos quartos",
            summary = "Buscar todos quartos",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200")
            }
    )
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<RoomDTO>>getAll(Pageable pageable){
        return ResponseEntity.ok().body(roomService.findAll(pageable));
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description ="Buscar quarto por ID",
            summary = "Buscar quarto por ID",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            }
    )
    public ResponseEntity<RoomDTO>getById(@PathVariable Long id){
        return ResponseEntity.ok().body(roomService.findById(id));
    }


    @PostMapping(produces = "application/json")
    @Operation(
            description ="Criar novo quarto",
            summary = "Criar novo quarto",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbbiden", responseCode = "403"),
                    @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
            }
    )
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<RoomDTO>insert(@Valid @RequestBody RoomDTO dto){
        RoomDTO result = roomService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @Operation(
            description ="Atualizar quarto pelo ID",
            summary = "Atualizar quarto pelo ID",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbbiden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
            }
    )
    @PutMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<RoomDTO>update(@PathVariable Long id,@Valid @RequestBody RoomDTO dto){
        return ResponseEntity.ok().body(roomService.update(id, dto));
    }

    @Operation(
            description ="Deletar quarto pelo ID",
            summary = "Deletar quarto pelo ID",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbbiden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
            }
    )
    @DeleteMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<RoomDTO>delete(@PathVariable Long id){
        return ResponseEntity.ok().body(roomService.delete(id));
    }


}
