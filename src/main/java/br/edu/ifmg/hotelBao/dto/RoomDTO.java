package br.edu.ifmg.hotelBao.dto;

import br.edu.ifmg.hotelBao.entitie.Room;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class RoomDTO extends RepresentationModel<RoomDTO> {
    private long id;
    private String description;
    @Positive
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



}
