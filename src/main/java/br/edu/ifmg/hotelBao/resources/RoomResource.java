package br.edu.ifmg.hotelBao.resources;

import br.edu.ifmg.hotelBao.dto.RoomDTO;
import br.edu.ifmg.hotelBao.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping(value = "/room")
@Tag(name = "Room", description = "Controller/Resource for rooms")
public class RoomResource {

    @Autowired
    RoomService roomService;

    @Operation(
            description ="Get all room",
            summary = "Get all room",
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
            description ="Get a room",
            summary = "Get a room",
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
            description ="Create a new room",
            summary = "Create a new room",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbbiden", responseCode = "403")
            }
    )
    public ResponseEntity<RoomDTO>insert( @RequestBody RoomDTO dto){
        RoomDTO result = roomService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @Operation(
            description ="Update a room",
            summary = "Update a room",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbbiden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            }
    )
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<RoomDTO>update(@PathVariable Long id,@RequestBody RoomDTO dto){
        return ResponseEntity.ok().body(roomService.update(id, dto));
    }

    @Operation(
            description ="Delete a room",
            summary = "Delete a room",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbbiden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
            }
    )
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<RoomDTO>delete(@PathVariable Long id){
        return ResponseEntity.ok().body(roomService.delete(id));
    }


}
