package com.cinemaebooking.backend.room.application.mapper;

import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.domain.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomResponseMapper {
    public RoomResponse toRoomResponse(Room room){
        if(room == null) return null;
        return new RoomResponse(
                room.getId() != null ? room.getId().getValue() : null,
                room.getName(),
                room.getRoomType(),
                room.getNumberOfRows(),
                room.getNumberOfCols(),
                room.getTotalSeats(),
                room.getStatus(),
                room.getCinemaId()
        );
    }
}
