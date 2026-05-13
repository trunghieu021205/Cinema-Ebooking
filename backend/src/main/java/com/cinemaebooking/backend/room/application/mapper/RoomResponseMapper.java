package com.cinemaebooking.backend.room.application.mapper;

import com.cinemaebooking.backend.common.exception.domain.RoomLayoutExceptions;
import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.domain.enums.RoomType;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room_layout.application.port.roomLayout.RoomLayoutRepository;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RoomResponseMapper {
    private final RoomLayoutRepository layoutRepository;

    public RoomResponse toRoomResponse(Room room, RoomLayout currentLayout){
        if(room == null) return null;

        RoomType resolvedType = (currentLayout != null)
                ? currentLayout.getRoomType()
                : room.getRoomType();
        return new RoomResponse(
                room.getId() != null ? room.getId().getValue() : null,
                room.getName(),
                resolvedType,
                room.getNumberOfRows(),
                room.getNumberOfCols(),
                room.getTotalSeats(),
                room.getStatus(),
                room.getCinemaId()
        );
    }
}
