package br.edu.ifmg.hotelBao.repository;

import br.edu.ifmg.hotelBao.entitie.Room;
import br.edu.ifmg.hotelBao.entitie.Stay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM Stay s " +
            "WHERE s.room = :room " +
            "AND s.checkIn < :checkOut " +
            "AND s.checkOut > :checkIn")
    boolean existsReservationConflict(Room room, Instant checkIn, Instant checkOut);

    // Consulta para encontrar a estadia de maior valor de um cliente
    @Query("SELECT MAX(s.room.price) " +
            "FROM Stay s " +
            "WHERE s.client.id = :clientId")
    Double findHighestValueStayByClient(@Param("clientId") Long clientId);

    // Consulta para encontrar a estadia de menor valor de um cliente
    @Query("SELECT MIN(s.room.price) " +
            "FROM Stay s " +
            "WHERE s.client.id = :clientId")
    Double findLowestValueStayByClient(@Param("clientId") Long clientId);

    // Consulta para calcular o valor total das estadias de um cliente
    @Query("SELECT SUM(s.room.price) " +
            "FROM Stay s " +
            "WHERE s.client.id = :clientId")
    Double findTotalValueByClient(@Param("clientId") Long clientId);

    @Query("SELECT s FROM Stay s WHERE s.client.id = :clientId")
    List<Stay> findByClientId(@Param("clientId") Long clientId);

}