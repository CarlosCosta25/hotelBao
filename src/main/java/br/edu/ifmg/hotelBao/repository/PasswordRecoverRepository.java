package br.edu.ifmg.hotelBao.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.ifmg.hotelBao.entitie.PasswordRecover;

@Repository
public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {
    @Query("SELECT obj FROM PasswordRecover obj " +
            "WHERE (obj.token = :token)" +
            "AND (obj.expiration > :now)")
    List<PasswordRecover> searchValidToken (String token, Instant now);
}
