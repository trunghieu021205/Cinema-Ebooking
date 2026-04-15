package com.cinemaebooking.backend.room.infrastructure.adapter;

import com.cinemaebooking.backend.cinema.infrastructure.persistence.repository.CinemaJpaRepository;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.room.infrastructure.mapper.RoomMapper;
import com.cinemaebooking.backend.room.infrastructure.persistence.repository.RoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomJpaRepository roomJpaRepository;
    private final RoomMapper roomMapper;
    private final CinemaJpaRepository cinemaJpaRepository;

    @Override
    public Room create(Room room) {
        var cinema = cinemaJpaRepository.findById(room.getCinemaId())
                .orElseThrow();

        var newEntity = roomMapper.toEntity(room);
        newEntity.setCinema(cinema);

        return roomMapper.toDomain(roomJpaRepository.save(newEntity));
    }

    @Override
    public Room update(Room room) {
        var oldEntity = roomJpaRepository.findById(room.getId().getValue())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        var cinema = cinemaJpaRepository.findById(room.getCinemaId())
                .orElseThrow();

        oldEntity.setName(room.getName());
        oldEntity.setTotalSeats(room.getTotalSeats());
        oldEntity.setRoomType(room.getRoomType());
        oldEntity.setStatus(room.getStatus());
        oldEntity.setCinema(cinema);

        return roomMapper.toDomain(roomJpaRepository.save(oldEntity));
    }


    @Override
    public Optional<Room> findById(RoomId id) {
        return roomJpaRepository.findById(id.getValue())
                .map(roomMapper::toDomain);
    }

    @Override
    public Page<Room> findAll(Pageable pageable) {
        return roomJpaRepository.findAll(pageable)
                .map(roomMapper::toDomain);
    }

    @Override
    public void deleteById(RoomId id) {
        var entity = roomJpaRepository.findById(id.getValue())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        entity.setDeletedAt(LocalDateTime.now());
        roomJpaRepository.save(entity);
    }

    @Override
    public boolean existsById(RoomId id) {
        return roomJpaRepository.existsById(id.getValue());
    }

    @Override
    public boolean existsByName(String name) {
        return roomJpaRepository.existsByName(name);
    }

    @Override
    public Page<Room> findByCinemaId(Long cinemaId, Pageable pageable) {

        if (cinemaId == null) {
            throw new IllegalArgumentException("CinemaId must not be null");
        }

        return roomJpaRepository.findByCinema_Id(cinemaId, pageable)
                .map(roomMapper::toDomain);
    }

    @Override
    public boolean existsByNameAndCinemaId(String name,Long CinemaId){return roomJpaRepository.existsByNameAndCinemaId(name,CinemaId);}
}