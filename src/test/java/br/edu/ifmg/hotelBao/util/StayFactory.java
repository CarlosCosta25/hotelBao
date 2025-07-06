package br.edu.ifmg.hotelBao.util;

import br.edu.ifmg.hotelBao.dto.StayDTO;
import br.edu.ifmg.hotelBao.entitie.Stay;

import java.time.Instant;

public class StayFactory {

    public static Stay createStay() {
        Stay stay = new Stay();
        stay.setCheckIn(Instant.now().plusSeconds(20));
        stay.setCheckOut(Instant.now().plusSeconds(86400));
        stay.setClient(ClientFactory.createClientWithId(1L));
        stay.setRoom(RoomFactory.createRoomWithId(1L));
        return stay;
    }

    public static StayDTO createStayDTO() {
        return new StayDTO(createStay());
    }
}
