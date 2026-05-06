package com.cinemaebooking.backend.room_layout.application.port.roomLayout;

import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayout.RoomLayoutId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomLayoutRepository {

    RoomLayout create(RoomLayout roomLayout);

    Optional<RoomLayout> findById(RoomLayoutId id);

    Optional<RoomLayout> findCurrentByRoomIdAndDate(Long roomId, LocalDate date);

    Optional<RoomLayout> findLatestByRoomId(Long roomId);

    boolean existsByRoomId(Long roomId);

    List<RoomLayout> findAllByRoomIdOrderByLayoutVersionDesc(Long roomId);

    Optional<RoomLayout> findByRoomIdAndLayoutVersion(Long roomId, Integer version);

}
