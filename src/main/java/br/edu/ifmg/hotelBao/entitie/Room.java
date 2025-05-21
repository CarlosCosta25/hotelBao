package br.edu.ifmg.hotelBao.entitie;

import br.edu.ifmg.hotelBao.dto.RoomDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name ="room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "TEXT")
    private String description;
    private double price;
    private String imageUrl;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt;

    public Room(long id, String description, double price, String imageUrl) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }
    public Room() {
    }
    public Room(Room room) {
        this.id = room.getId();
        this.description = room.getDescription();
        this.price = room.getPrice();
        this.imageUrl = room.getImageUrl();
    }
    public Room(RoomDTO dto){
        this.id = dto.getId();
        this.description = dto.getDescription();
        this.price = dto.getPrice();
        this.imageUrl = dto.getImageUrl();
    }
}
