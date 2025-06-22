package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.dto.ClientDTO;
import br.edu.ifmg.hotelBao.dto.ClientInsertDTO;

import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.entitie.Role;
import br.edu.ifmg.hotelBao.service.exceptions.DataBaseException;
import br.edu.ifmg.hotelBao.service.exceptions.ResourceNotFound;
import br.edu.ifmg.hotelBao.projections.ClientDetailsProjection;
import br.edu.ifmg.hotelBao.repository.ClientRepository;
import br.edu.ifmg.hotelBao.repository.RoleRepository;
import br.edu.ifmg.hotelBao.resources.ClientResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class ClientService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

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
                        new ResourceNotFound("Client from id: " + id + "not found")
                )
        )
                .add(linkTo(methodOn(ClientResource.class).getById(id)).withSelfRel())
                .add(linkTo(methodOn(ClientResource.class).getAll(null)).withRel("Get all clients"))
                .add(linkTo(methodOn(ClientResource.class).update(id, new ClientDTO(obj.get()))).withRel("Update a client"));
    }

    @Transactional
    public ClientDTO insert(ClientInsertDTO dto) {
        Client entity = new Client();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setLogin(dto.getLogin());

        // Codificar a senha ANTES de salvar
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

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

            return new ClientDTO(
                    clientRepository.
                            save(
                                    new Client(
                                            client.getId(),
                                            dto.getName(),
                                            dto.getEmail(),
                                            dto.getPhone(),
                                            client.getLogin(),
                                            client.getPassword(),
                                            client.getCreatedAt(),
                                            client.getUpdatedAt()
                                    )
                            )
            )
                    .add(linkTo(methodOn(ClientResource.class).getById(client.getId())).withRel("find a client"))
                    .add(linkTo(methodOn(ClientResource.class).getAll(null)).withRel("All client"))
                    .add(linkTo(methodOn(ClientResource.class).delete(client.getId())).withRel("Delete client"));

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFound("Cliente com o id: " + id + " não existe");
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        List<ClientDetailsProjection> resultado = clientRepository.searchUserAndRoleByUsername(username);
        if (resultado.isEmpty()) {
            throw new UsernameNotFoundException("Usuario nao encontrado");
        }
        Client client = new Client();
        client.setLogin(resultado.get(0).getUsername());
        client.setPassword(resultado.get(0).getPassword());
        for(ClientDetailsProjection entity : resultado) {
            client.addRole(new Role(entity.getRoleId(), entity.getAuthority()));
        }
        return client;
    }


    @Transactional
    public ClientDTO signUp(ClientInsertDTO dto) {
        Client entity = new Client();
        copyDtoToEntity(dto, entity);

        //  Busca segura da role padrão
        Role role = roleRepository.findByAuthority("ROLE_CLIENT");


        entity.getRoles().clear();
        entity.getRoles().add(role);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        Client saved = clientRepository.save(entity);
        return new ClientDTO(saved);
    }

    private void copyDtoToEntity(ClientInsertDTO dto, Client entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setLogin(dto.getLogin());
        // A senha é codificada fora, então não codifique aqui
    }

    }
