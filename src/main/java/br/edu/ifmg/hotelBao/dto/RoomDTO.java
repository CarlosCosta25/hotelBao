package br.edu.ifmg.hotelBao.dto;

import br.edu.ifmg.hotelBao.entitie.Room;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDTO {
    private long id;
    private String description;
    private double price;
    private String imageUrl;

    public RoomDTO(Room room) {
        this.id = room.getId();
        this.description = room.getDescription();
        this.price = room.getPrice();
        this.imageUrl = room.getImageUrl();
    }
    public RoomDTO() {
    }
    public RoomDTO(long id, String description, double price, String imageUrl) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

}
