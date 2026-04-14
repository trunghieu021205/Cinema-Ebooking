package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.room.application.dto.CreateRoomRequest;
import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.domain.Room;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRoomUseCase {

    private final RoomRepository roomRepository;

    @Transactional
    public RoomResponse execute(CreateRoomRequest request) {
        if (roomRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Room with name '" + request.getName() + "' already exists");
        }

        Room room = Room.create(
                request.getName(),
                request.getTotalSeats(),
                request.getRoomType(),
                request.getCinemaId()
        );

        Room saved = roomRepository.save(room);
        return RoomResponse.from(saved);
    }
}
