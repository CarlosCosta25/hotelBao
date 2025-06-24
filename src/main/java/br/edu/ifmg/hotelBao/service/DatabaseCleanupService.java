package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.entitie.PasswordRecover;
import br.edu.ifmg.hotelBao.entitie.Role;
import br.edu.ifmg.hotelBao.entitie.Room;
import br.edu.ifmg.hotelBao.entitie.Stay;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private StayService stayService;

    @Transactional
    public void cleanAllDataDatabase() {
        // ordem respeitando FKs
        entityManager.createQuery("DELETE FROM Stay").executeUpdate();
        entityManager.createQuery("DELETE FROM PasswordRecover").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM client_role").executeUpdate();
        entityManager.createQuery("DELETE FROM Client").executeUpdate();
        entityManager.createQuery("DELETE FROM Room").executeUpdate();
        entityManager.createQuery("DELETE FROM Role").executeUpdate();
        // reset de sequências
        try {
            entityManager.createNativeQuery("ALTER TABLE client ALTER COLUMN id RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE room ALTER COLUMN id RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE stay ALTER COLUMN id RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE role ALTER COLUMN id RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE password_recover ALTER COLUMN id RESTART WITH 1").executeUpdate();
        } catch (Exception ignored) {}
    }



}