package br.edu.ifmg.hotelBao.repository;

import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.projections.ClientDetailsProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
    Client findByEmailAndPassword(String email, String password);

    @Query(nativeQuery = true, value = """
        SELECT u.login as username,
               u.password,
               r.id as roleId,
               r.authority
        FROM client u
        INNER JOIN client_role ur ON u.id = ur.client_id
        INNER JOIN role r ON r.id = ur.role_id
        WHERE u.login = :username
    """
    )
    List<ClientDetailsProjection> searchUserAndRoleByUsername(String username);
}
