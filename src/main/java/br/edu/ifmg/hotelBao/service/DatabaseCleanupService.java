package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.entitie.PasswordRecover;
import br.edu.ifmg.hotelBao.entitie.Role;
import br.edu.ifmg.hotelBao.entitie.Room;
import br.edu.ifmg.hotelBao.entitie.Stay;
import br.edu.ifmg.hotelBao.repository.ClientRepository;
import br.edu.ifmg.hotelBao.repository.RoomRepository;
import br.edu.ifmg.hotelBao.repository.StayRepository;
import br.edu.ifmg.hotelBao.service.exceptions.DataBaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class DatabaseCleanupService {


    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private StayRepository stayRepository;

    @Transactional
    public void cleanAllDataDatabase() {

        try{
            stayRepository.deleteAll();
            clientRepository.deleteAll();
            roomRepository.deleteAll();

        }catch (DataBaseException e) {
            throw new DataBaseException("Erro ao limpar os dados do banco de dados: " + e.getMessage());
        }
    }



}