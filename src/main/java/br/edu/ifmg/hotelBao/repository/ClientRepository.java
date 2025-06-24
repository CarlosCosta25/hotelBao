package br.edu.ifmg.hotelBao.repository;

import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.projections.ClientDetailsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);


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

    @Query("SELECT DISTINCT c FROM Client c JOIN c.roles r WHERE r.authority = 'ROLE_CLIENT'")
    Page<Client> findAllClients(Pageable pageable);

    @Query("SELECT c FROM Client c JOIN c.roles r WHERE c.id = :id AND r.authority = 'ROLE_CLIENT'")
    Optional<Client> findByIdClient(@Param("id") Long id);
}
