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
    public Map<String, Object> cleanAllData() {
        Map<String, Object> result = new HashMap<>();

        try {
            // Contar registros antes da limpeza
            Map<String, Long> beforeStats = getDatabaseStats();
            result.put("recordsBeforeCleanup", beforeStats);

            // Limpar tabelas respeitando as dependências (ordem é importante)
            int staysDeleted = cleanStays();
            int passwordRecoverDeleted = cleanPasswordRecover();
            int clientRoleDeleted = cleanClientRoleAssociations();
            int clientsDeleted = cleanClients();
            int roomsDeleted = cleanRooms();
            int rolesDeleted = cleanRoles();

            // Reset sequences/auto-increment
            resetSequences();

            // Estatísticas após limpeza
            Map<String, Long> afterStats = getDatabaseStats();

            result.put("success", true);
            result.put("message", "Banco de dados limpo com sucesso");
            result.put("deletedRecords", Map.of(
                    "stays", staysDeleted,
                    "passwordRecovers", passwordRecoverDeleted,
                    "clientRoles", clientRoleDeleted,
                    "clients", clientsDeleted,
                    "rooms", roomsDeleted,
                    "roles", rolesDeleted
            ));
            result.put("recordsAfterCleanup", afterStats);
            result.put("timestamp", Instant.now());

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Erro ao limpar banco de dados: " + e.getMessage());
            throw new DataBaseException("Erro ao limpar banco de dados", e);
        }

        return result;
    }



    private int cleanStays() {
        return entityManager.createQuery("DELETE FROM Stay").executeUpdate();
    }

    private int cleanPasswordRecover() {
        return entityManager.createQuery("DELETE FROM PasswordRecover").executeUpdate();
    }

    private int cleanClientRoleAssociations() {
        // Limpar associações many-to-many
        return entityManager.createNativeQuery("DELETE FROM client_role").executeUpdate();
    }

    private int cleanClients() {
        return entityManager.createQuery("DELETE FROM Client").executeUpdate();
    }

    private int cleanRooms() {
        return entityManager.createQuery("DELETE FROM Room").executeUpdate();
    }

    private int cleanRoles() {
        return entityManager.createQuery("DELETE FROM Role").executeUpdate();
    }

    private void resetSequences() {
        try {
            entityManager.createNativeQuery("ALTER TABLE client ALTER COLUMN id RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE room ALTER COLUMN id RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE stay ALTER COLUMN id RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE role ALTER COLUMN id RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE password_recover ALTER COLUMN id RESTART WITH 1").executeUpdate();

        } catch (Exception e) {
            // Log warning but don't fail the operation
            System.out.println("Warning: Could not reset sequences: " + e.getMessage());
        }
    }

    public Map<String, Long> getDatabaseStats() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("clients", getTableCount("Client"));
        stats.put("rooms", getTableCount("Room"));
        stats.put("stays", getTableCount("Stay"));
        stats.put("roles", getTableCount("Role"));
        stats.put("passwordRecovers", getTableCount("PasswordRecover"));
        stats.put("clientRoleAssociations", getTableCountNative("client_role"));

        return stats;
    }

    private Long getTableCount(String entityName) {
        return (Long) entityManager.createQuery("SELECT COUNT(*) FROM " + entityName)
                .getSingleResult();
    }

    private Long getTableCountNative(String tableName) {
        return ((Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM " + tableName)
                .getSingleResult()).longValue();
    }




}