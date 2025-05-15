package br.edu.ifmg.hotelBao.resources;

import br.edu.ifmg.hotelBao.dto.ClientDTO;
import org.springframework.web.bind.annotation.*;
import br.edu.ifmg.hotelBao.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/client")
public class ClientResource {

    @Autowired
    ClientService clientService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<ClientDTO>> getAllClients(Pageable pageable) {
        Page<ClientDTO> clients = clientService.findAll(pageable);
        return ResponseEntity.ok(clients);
    }
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ClientDTO>getClient(@PathVariable long id){
        return ResponseEntity.ok(clientService.findById(id));
    }

    @PostMapping( produces = "application/json")
    public ResponseEntity<ClientDTO>insertClient(@RequestBody ClientDTO dto){
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(clientService.insert(dto));
    }


}
