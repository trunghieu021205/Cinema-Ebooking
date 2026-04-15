package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.dto.UpdateRoomRequest;
import com.cinemaebooking.backend.room.application.mapper.RoomResponseMapper;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateRoomUseCase {

    private final RoomRepository roomRepository;
    private final RoomResponseMapper mapper;

    public RoomResponse execute(RoomId id, UpdateRoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room with id: " + id + " not found!"));
        // Update các field
        room.setName(request.getName());
        room.setRoomType(request.getRoomType());
        room.setTotalSeats(request.getTotalSeats());
        room.setStatus(request.getStatus());

        Room roomSaved = roomRepository.save(room);
        return mapper.toRoomResponse(roomSaved);
    }
}