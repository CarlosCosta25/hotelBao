package br.edu.ifmg.hotelBao.repository;

import br.edu.ifmg.hotelBao.entitie.Client;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
