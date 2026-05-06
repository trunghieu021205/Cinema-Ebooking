package com.cinemaebooking.backend.room.application.port;

import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RoomRepository {

    Room create(Room room);

    Room update(Room room);

    Optional<Room> findById(RoomId id);

    Page<Room> findAll(Pageable pageable);

    void deleteById(RoomId id);

    boolean existsById(RoomId id);

    boolean existsByName(String name);

    Page<Room> findByCinemaId(Long cinemaId, Pageable pageable);

    boolean existsByCinemaId(Long cinemaId);

    boolean existsByNameAndCinemaId(String name, Long cinemaId);

    boolean existsByNameAndCinemaIdAndIdNot(String name, Long cinemaId, RoomId id);

    Long getCinemaIdByRoomId(RoomId roomId);

}
