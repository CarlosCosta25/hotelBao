package br.edu.ifmg.hotelBao.resources;

import br.edu.ifmg.hotelBao.dto.RoomDTO;
import br.edu.ifmg.hotelBao.service.RoomService;
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
public class RoomResource {

    @Autowired
    RoomService roomService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<RoomDTO>>getAll(Pageable pageable){
        return ResponseEntity.ok().body(roomService.findAll(pageable));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<RoomDTO>getById(@PathVariable Long id){
        return ResponseEntity.ok().body(roomService.findById(id));
    }
    @PostMapping(produces = "application/json")
    public ResponseEntity<RoomDTO>insert( @RequestBody RoomDTO dto){
        RoomDTO result = roomService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<RoomDTO>update(@PathVariable Long id,@RequestBody RoomDTO dto){
        return ResponseEntity.ok().body(roomService.update(id, dto));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<RoomDTO>delete(@PathVariable Long id){
        return ResponseEntity.ok().body(roomService.delete(id));
    }


}
