package br.edu.ifmg.hotelBao.repository;

import br.edu.ifmg.hotelBao.entitie.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByAuthority(String authority);
}
