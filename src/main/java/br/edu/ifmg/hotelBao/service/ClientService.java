package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.dto.ClientDTO;
import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.exceptions.DataBaseException;
import br.edu.ifmg.hotelBao.exceptions.ResourceNotFoud;
import br.edu.ifmg.hotelBao.repository.ClientRepository;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        Page<Client> entitys = clientRepository.findAll(pageable);
        return entitys.map(ClientDTO::new);
    }
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Optional<Client> obj = clientRepository.findById(id);
        return new ClientDTO(
                obj.orElseThrow(() -> new ResourceNotFoud("Client from id: "+ id +"not found"))
        );
    }

    @Transactional
    public ClientDTO insert(ClientDTO dto){
        Client entity = new Client();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        Client result = clientRepository.save(entity);
        return new ClientDTO(result);
    }

    @Transactional
    public ClientDTO update(long id,ClientDTO dto){
        if(!clientRepository.existsById(id)) {
            throw new RuntimeException("Client from id: "+ id +"not found");
        }
    Client entity = clientRepository.save(new Client(dto));
            return new ClientDTO(entity);
    }
    @Transactional
    public ClientDTO delete(long id){
        if(!clientRepository.existsById(id)) {
            throw new RuntimeException("Client from id: "+ id +"not found");
        }
        try {
            Client entity = clientRepository.getReferenceById(id);
            clientRepository.delete(entity);
            return new ClientDTO(entity);
        }catch (Exception e){
            throw new DataBaseException("Integrity violation");
        }
    }



}
