package br.edu.ifmg.hotelBao.dto;

import br.edu.ifmg.hotelBao.entitie.Stay;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class StayDTO extends RepresentationModel<StayDTO> {
    private Long id;

    /** Data/hora de check‑in */
    @NotNull
    @FutureOrPresent
    private Instant checkIn;

    /** Data/hora de check‑out (calculado) */
    private Instant checkOut;

    @Valid
    @NotNull
    private ClientDTO client;
    @Valid
    @NotNull
    private RoomDTO room;



    public StayDTO(Stay stay) {
        this.id       = stay.getId();
        this.client = new ClientDTO(stay.getClient());
        this.room   = new RoomDTO(stay.getRoom());
        this.checkIn  = stay.getCheckIn();
        this.checkOut = stay.getCheckOut();
    }


}