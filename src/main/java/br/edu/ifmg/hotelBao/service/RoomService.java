package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.dto.RoomDTO;
import br.edu.ifmg.hotelBao.entitie.Room;
import br.edu.ifmg.hotelBao.exceptions.DataBaseException;
import br.edu.ifmg.hotelBao.exceptions.ResourceNotFoud;
import br.edu.ifmg.hotelBao.repository.RoomRepository;
import br.edu.ifmg.hotelBao.resources.RoomResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public Page<RoomDTO> findAll(Pageable pageable) {

        Page<Room> list = roomRepository.findAll(pageable);
        return list.map(
                room -> new RoomDTO(room)
                        .add(linkTo(methodOn(RoomResource.class).getAll(null)).withSelfRel())
                        .add(linkTo(methodOn(RoomResource.class).getById(room.getId())).withRel("Get a room"))
        );
    }

    @Transactional(readOnly = true)
    public RoomDTO findById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoud("Room id: " + id + " not found")
        );
        return new RoomDTO(room).add(
                linkTo(methodOn(RoomResource.class).getById(id)).withSelfRel())
                .add(linkTo(methodOn(RoomResource.class).update(id, new RoomDTO(room))).withRel("Update a room"))
                .add(linkTo(methodOn(RoomResource.class).delete(room.getId())).withRel("Delete a room"));
    }

    @Transactional
    public RoomDTO insert(RoomDTO dto) {
        Room entity = new Room();
        copyDTOtoEntity(dto, entity);
        entity = roomRepository.save(entity);
        return new RoomDTO(entity)
                .add(linkTo(methodOn(RoomResource.class).getById(entity.getId())).withRel("Get a room"))
                .add(linkTo(methodOn(RoomResource.class).getAll(null)).withRel("All rooms"))
                .add(linkTo(methodOn(RoomResource.class).update(entity.getId(), new RoomDTO(entity))).withRel("Upadate room"))
                .add(linkTo(methodOn(RoomResource.class).delete(entity.getId())).withRel("Delete room"));
    }

    @Transactional
    public RoomDTO update(Long id, RoomDTO dto) {
        try {
            Room entity = roomRepository.getReferenceById(id);
            copyDTOtoEntity(dto, entity);
            return new RoomDTO(entity)
                    .add(linkTo(methodOn(RoomResource.class).getById(entity.getId())).withSelfRel())
                    .add(linkTo(methodOn(RoomResource.class).getAll(null)).withRel("All rooms"))
                    .add(linkTo(methodOn(RoomResource.class).delete(entity.getId())).withRel("Delete room"));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoud("Room id: " + id + " not found");
        }

    }

    @Transactional
    public RoomDTO delete(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoud("Room id: " + id + " not found");
        }
        try {
            Room entity = roomRepository.getReferenceById(id);
            roomRepository.deleteById(id);
            return new RoomDTO(entity);

        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    private void copyDTOtoEntity(RoomDTO dto, Room entity) {
        entity.setImageUrl(dto.getImageUrl());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
    }
}
