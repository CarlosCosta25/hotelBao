package br.edu.ifmg.hotelBao.util;

import br.edu.ifmg.hotelBao.dto.RoomDTO;
import br.edu.ifmg.hotelBao.entitie.Room;
import jakarta.validation.constraints.Positive;

public class RoomFactory {
    public static Room createRoom() {
        Room room = new Room();
        room.setDescription("Quarto de Teste");
        room.setPrice(100.0);
        room.setImageUrl("https://example.com/room.jpg");
        return room;
    }

    public static RoomDTO createRoomDTO() {
        return new RoomDTO(createRoom());
    }
}
