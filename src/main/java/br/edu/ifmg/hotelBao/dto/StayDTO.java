package br.edu.ifmg.hotelBao.dto;

import br.edu.ifmg.hotelBao.entitie.Stay;
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
    private long id;

    /** Relações por ID no input e output simplificado */
    @NotNull
    private Long clientId;

    @NotNull
    private Long roomId;

    /** Data/hora de check‑in */
    @NotNull
    @FutureOrPresent
    private Instant checkIn;

    /** Data/hora de check‑out (calculado) */
    private Instant checkOut;

    public StayDTO(Stay stay) {
        this.id       = stay.getId();
        this.clientId = stay.getClient().getId();
        this.roomId   = stay.getRoom().getId();
        this.checkIn  = stay.getCheckIn();
        this.checkOut = stay.getCheckOut();
    }

    public StayDTO(long id, Long clientId, Long roomId, Instant checkIn, Instant checkOut) {
        this.id       = id;
        this.clientId = clientId;
        this.roomId   = roomId;
        this.checkIn  = checkIn;
        this.checkOut = checkOut;
    }
}