package br.edu.ifmg.hotelBao.repository;

import br.edu.ifmg.hotelBao.entitie.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}