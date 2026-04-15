package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.room.application.dto.CreateRoomRequest;
import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.mapper.RoomResponseMapper;
import com.cinemaebooking.backend.room.domain.enums.RoomStatus;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRoomUseCase {

    private final RoomRepository roomRepository;
    private final RoomResponseMapper mapper;

    public RoomResponse execute(CreateRoomRequest request) {

        if (roomRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Room with name '" + request.getName() + "' already exists");
        }

        Room room = Room.builder()
                .name(request.getName())
                .totalSeats(request.getTotalSeats())
                .roomType(request.getRoomType())
                .status(RoomStatus.ACTIVE)
                .cinemaId(request.getCinemaId())
                .build();

        Room saved = roomRepository.create(room);
        return mapper.toRoomResponse(saved);
    }
}