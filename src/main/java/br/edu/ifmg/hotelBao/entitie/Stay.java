package br.edu.ifmg.hotelBao.entitie;

import br.edu.ifmg.hotelBao.dto.StayDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stay")
public class Stay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Relacionamento para o cliente que fará a estadia
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    // Relacionamento para o quarto reservado
    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    // Data/hora de início da estadia
    @Column(name = "check_in", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
    private Instant checkIn;

    // Data/hora de término da estadia (sempre um dia depois)
    @Column(name = "check_out", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
    private Instant checkOut;





    public Stay(Client client, Room room, Instant checkIn) {
        this.client = client;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkIn.plus(1, ChronoUnit.DAYS);
    }
}