package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.dto.StayDTO;
import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.entitie.Room;
import br.edu.ifmg.hotelBao.entitie.Stay;
import br.edu.ifmg.hotelBao.exceptions.CheckinException;
import br.edu.ifmg.hotelBao.exceptions.DataBaseException;
import br.edu.ifmg.hotelBao.exceptions.ResourceNotFoud;
import br.edu.ifmg.hotelBao.repository.ClientRepository;
import br.edu.ifmg.hotelBao.repository.RoomRepository;
import br.edu.ifmg.hotelBao.repository.StayRepository;
import br.edu.ifmg.hotelBao.resources.StayResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class StayService {

    @Autowired private StayRepository stayRepository;
    @Autowired private ClientRepository clientRepository;
    @Autowired private RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public Page<StayDTO> findAll(Pageable pageable) {
        return stayRepository.findAll(pageable)
                .map(stay -> new StayDTO(stay)
                        .add(linkTo(methodOn(StayResource.class).getById(stay.getId())).withSelfRel())
                        .add(linkTo(methodOn(StayResource.class).getAll(null)).withRel("Get all stays")));
    }

    @Transactional(readOnly = true)
    public StayDTO findById(Long id) {
        Stay stay = stayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoud("Stay id: " + id + " not found"));
        return new StayDTO(stay)
                .add(linkTo(methodOn(StayResource.class).getById(id)).withSelfRel())
                .add(linkTo(methodOn(StayResource.class).getAll(null)).withRel("Get all stays"));
    }

    @Transactional
    public StayDTO insert(StayDTO dto) {
        // 1) carrega as entidades
        Client client = clientRepository.getReferenceById(dto.getClientId());
        Room   room   = roomRepository.getReferenceById(dto.getRoomId());


        Instant checkIn = dto.getCheckIn();
        Instant checkOut = checkIn.plus(1, ChronoUnit.DAYS);

        if (stayRepository.existsReservationConflict(room, checkIn, checkOut)) {
            throw new CheckinException("Quarto " + room.getId() + " já está reservado nesse período.");
        }

        // 4) cria e salva normalmente
        Stay stay  = new Stay(client, room, dto.getCheckIn());
        Stay saved = stayRepository.save(stay);

        return new StayDTO(saved)
                .add(linkTo(methodOn(StayResource.class).getById(saved.getId())).withRel("Get stay"))
                .add(linkTo(methodOn(StayResource.class).getAll(null)).withRel("Get all stays"));
    }

    @Transactional
    public StayDTO update(Long id, StayDTO dto) {
        try {
            Stay stay    = stayRepository.getReferenceById(id);
            Client client = clientRepository.getReferenceById(dto.getClientId());
            Room room     = roomRepository.getReferenceById(dto.getRoomId());

            Instant checkIn = dto.getCheckIn();
            Instant checkOut = checkIn.plus(1, ChronoUnit.DAYS);

            if (stayRepository.existsReservationConflict(room, checkIn, checkOut)) {
                throw new CheckinException("Quarto " + room.getId() + " já está reservado nesse período.");
            }

            stay.setClient(client);
            stay.setRoom(room);
            stay.setCheckIn(dto.getCheckIn());
            stay.setCheckOut(dto.getCheckIn().plus(1, ChronoUnit.DAYS));

            Stay updated = stayRepository.save(stay);
            return new StayDTO(updated)
                    .add(linkTo(methodOn(StayResource.class).getById(id)).withSelfRel())
                    .add(linkTo(methodOn(StayResource.class).getAll(null)).withRel("Get all stays"));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoud("Stay, Client or Room not found");
        }
    }

    @Transactional
    public StayDTO delete(Long id) {
        if (!stayRepository.existsById(id))
            throw new ResourceNotFoud("Stay id: " + id + " not found");
        Stay stay = stayRepository.getReferenceById(id);
        stayRepository.delete(stay);
        return new StayDTO(stay);
    }
}