package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.dto.StayDTO;
import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.entitie.Room;
import br.edu.ifmg.hotelBao.entitie.Stay;
import br.edu.ifmg.hotelBao.service.exceptions.CheckinException;
import br.edu.ifmg.hotelBao.service.exceptions.ResourceNotFound;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

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
                        .add(linkTo(methodOn(StayResource.class).getAll(null)).withSelfRel())
                        .add(linkTo(methodOn(StayResource.class).getById(stay.getId())).withRel("Get stay")));
    }

    @Transactional(readOnly = true)
    public StayDTO findById(Long id) {
        Stay stay = stayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Stay id: " + id + " not found"));
        return new StayDTO(stay)
                .add(linkTo(methodOn(StayResource.class).getById(id)).withSelfRel())
                .add(linkTo(methodOn(StayResource.class).getAll(null)).withRel("Get all stays"));
    }

    @Transactional
    public StayDTO insert(StayDTO dto) {
        // 1) carrega as entidades
        Client client = clientRepository.getReferenceById(dto.getClient().getId());
        Room   room   = roomRepository.getReferenceById(dto.getRoom().getId());


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
            Client client = clientRepository.getReferenceById(dto.getClient().getId());
            Room room     = roomRepository.getReferenceById(dto.getRoom().getId());

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
            throw new ResourceNotFound("Stay, Client or Room not found");
        }
    }

    @Transactional
    public StayDTO delete(Long id) {
        if (!stayRepository.existsById(id))
            throw new ResourceNotFound("Stay id: " + id + " not found");
        Stay stay = stayRepository.getReferenceById(id);
        stayRepository.delete(stay);
        return new StayDTO(stay);
    }

    @Transactional(readOnly = true)
    public List<StayDTO> findByClientId(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFound("Client id: " + clientId + " not found");
        }
        List<Stay> list = stayRepository.findByClientId(clientId);
        return list.stream()
                .map(stay -> new StayDTO(stay))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public BigDecimal getTotalValueByClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFound("Client id: " + clientId + " not found");
        }
        BigDecimal totalValue = stayRepository.findTotalValueByClient(clientId);
        return totalValue != null ? totalValue : BigDecimal.ZERO;
    }


    @Transactional(readOnly = true)
    public StayDTO getHighestValueStayByClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFound("Client id: " + clientId + " not found");
        }
        Stay highestValueStay = stayRepository.findHighestValueStayByClient(clientId);
        if (highestValueStay == null) {
            throw new ResourceNotFound("No stays found for client id: " + clientId);
        }
        return new StayDTO(highestValueStay)
                .add(linkTo(methodOn(StayResource.class).getById(highestValueStay.getId())).withRel("Get stay"))
                .add(linkTo(methodOn(StayResource.class).getByClient(clientId)).withRel("Get all client stays"));
    }


    @Transactional(readOnly = true)
    public StayDTO getLowestValueStayByClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFound("Client id: " + clientId + " not found");
        }
        Stay lowestValueStay = stayRepository.findLowestValueStayByClient(clientId);
        if (lowestValueStay == null) {
            throw new ResourceNotFound("No stays found for client id: " + clientId);
        }
        return new StayDTO(lowestValueStay)
                .add(linkTo(methodOn(StayResource.class).getById(lowestValueStay.getId())).withRel("Get stay"))
                .add(linkTo(methodOn(StayResource.class).getByClient(clientId)).withRel("Get all client stays"));
    }
}