package br.edu.ifmg.hotelBao.repository;

import br.edu.ifmg.hotelBao.entitie.Room;
import br.edu.ifmg.hotelBao.entitie.Stay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM Stay s " +
            "WHERE s.room = :room " +
            "AND s.checkIn < :checkOut " +
            "AND s.checkOut > :checkIn")
    boolean existsReservationConflict(Room room, Instant checkIn, Instant checkOut);
}