package com.cinemaebooking.backend.room.application.port;

import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface RoomRepository {

    Room save(Room room);

    Optional<Room> findById(RoomId id);

    Page<Room> findAll(int page, int size);

    void deleteById(RoomId id);

    boolean existsById(RoomId id);

    boolean existsByName(String name);
}
