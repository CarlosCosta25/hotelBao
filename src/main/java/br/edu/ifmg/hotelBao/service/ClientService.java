package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.dto.ClientDTO;
import br.edu.ifmg.hotelBao.dto.ClientInsetDTO;
import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.exceptions.DataBaseException;
import br.edu.ifmg.hotelBao.exceptions.ResourceNotFoud;
import br.edu.ifmg.hotelBao.repository.ClientRepository;
import br.edu.ifmg.hotelBao.resources.ClientResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;


    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        Page<Client> entitys = clientRepository.findAll(pageable);

        return entitys.map(client -> new ClientDTO(client)

                .add(linkTo(methodOn(ClientResource.class).getAll(null)).withSelfRel())
                .add(linkTo(methodOn(ClientResource.class).getById(client.getId())).withRel("Get a client"))
        );
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Optional<Client> obj = clientRepository.findById(id);
        return new ClientDTO(
                obj.orElseThrow(() ->
                        new ResourceNotFoud("Client from id: " + id + "not found")
                )
        )
                .add(linkTo(methodOn(ClientResource.class).getById(id)).withSelfRel())
                .add(linkTo(methodOn(ClientResource.class).getAll(null)).withRel("Get all clients"))
                .add(linkTo(methodOn(ClientResource.class).update(id, new ClientDTO(obj.get()))).withRel("Update a client"));
    }

    @Transactional
    public ClientDTO insert(ClientInsetDTO dto) {
        Client entity = new Client();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setLogin(dto.getLogin());
        entity.setPassword(dto.getPassword());

        Client result = clientRepository.save(entity);
        return new ClientDTO(result)
                .add(linkTo(methodOn(ClientResource.class).getById(result.getId())).withRel("Get a client"))
                .add(linkTo(methodOn(ClientResource.class).getAll(null)).withRel("Get all clients"))
                .add(linkTo(methodOn(ClientResource.class).update(result.getId(), new ClientDTO(result))).withRel("Update a client"));
    }

    @Transactional
    public ClientDTO update(long id, ClientDTO dto) {
        try {
            Client client = clientRepository.getReferenceById(id);
            // atualiza apenas os campos que podem ser mudados
            client.setLogin(dto.getName());
            client.setName(dto.getName());
            client.setEmail(dto.getEmail());
            client.setPhone(dto.getPhone());
            // senha não é atualizáveis aqui
            Client saved = clientRepository.save(client);

            return new ClientDTO(saved)
                    .add( /* seus links HATEOAS */ );
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoud("Cliente com o id: " + id + " não existe");
        }
    }

    @Transactional
    public ClientDTO delete(long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Cliente com o id: " + id + " não existe");
        }
        try {
            Client entity = clientRepository.getReferenceById(id);
            clientRepository.delete(entity);
            return new ClientDTO(entity);
        } catch (Exception e) {
            throw new DataBaseException("Integridade do banco de dados foi violada");
        }
    }


}
