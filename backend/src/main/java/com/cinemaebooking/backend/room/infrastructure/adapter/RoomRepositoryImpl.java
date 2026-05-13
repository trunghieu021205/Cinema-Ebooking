package com.cinemaebooking.backend.room.infrastructure.adapter;

import com.cinemaebooking.backend.cinema.infrastructure.persistence.repository.CinemaJpaRepository;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.room.infrastructure.mapper.RoomMapper;
import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import com.cinemaebooking.backend.room.infrastructure.persistence.repository.RoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomJpaRepository roomJpaRepository;
    private final RoomMapper roomMapper;
    private final CinemaJpaRepository cinemaJpaRepository;

    @Override
    public Room create(Room room) {
        var cinema = cinemaJpaRepository.findByIdOrThrow(room.getCinemaId());

        var newEntity = roomMapper.toEntity(room);
        newEntity.setCinema(cinema);

        return roomMapper.toDomain(roomJpaRepository.save(newEntity));
    }

    @Override
    public Room update(Room room) {
        var oldEntity = roomJpaRepository.findByIdOrThrow(room.getId().getValue());

        var cinema = cinemaJpaRepository.findByIdOrThrow(room.getCinemaId());

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
        RoomJpaEntity room = roomJpaRepository.findByIdOrThrow(id.getValue());

        roomJpaRepository.delete(room);
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
    public boolean existsByCinemaId(Long cinemaId){
        return roomJpaRepository.existsByCinemaId(cinemaId);
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
    public boolean existsByNameAndCinemaId(String name,Long CinemaId){
        return roomJpaRepository.existsByNameAndCinemaId(name,CinemaId);
    }
    @Override
    public boolean existsByNameAndCinemaIdAndIdNot(String name, Long cinemaId, RoomId id){
        if (name == null || cinemaId == null || id == null) return false;
        return roomJpaRepository.existsByNameAndCinemaIdAndIdNot(
                name,
                cinemaId,
                id.getValue()
        );
    }

    @Override
    public Long getCinemaIdByRoomId(RoomId roomId) {
        return roomJpaRepository.getCinemaIdByRoomId(roomId.getValue());
    }
}
