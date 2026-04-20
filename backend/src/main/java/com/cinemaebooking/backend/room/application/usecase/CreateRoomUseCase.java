package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.room.application.dto.CreateRoomRequest;
import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.mapper.RoomResponseMapper;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.application.validator.RoomCommandValidator;
import com.cinemaebooking.backend.room.domain.enums.RoomStatus;
import com.cinemaebooking.backend.room.domain.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * CreateRoomUseCase - Handles creation of a new Room.
 * Flow:
 * 1. Validate request
 * 2. Build domain object
 * 3. Persist entity
 * 4. Map response
 */
@Service
@RequiredArgsConstructor
public class CreateRoomUseCase {

    private final RoomRepository roomRepository;
    private final RoomResponseMapper mapper;
    private final RoomCommandValidator validator;

    public RoomResponse execute(CreateRoomRequest request) {

        // ================== VALIDATION ==================
        validator.validateCreateRequest(request);

        // ================== BUILD DOMAIN ==================
        Room room = buildRoom(request);

        // ================== PERSIST ==================
        Room saved = roomRepository.create(room);

        // ================== RESPONSE ==================
        return mapper.toRoomResponse(saved);
    }

    // ================== PRIVATE METHODS ==================

    private Room buildRoom(CreateRoomRequest request) {
        return Room.builder()
                .name(request.getName())
                .totalSeats(request.getTotalSeats())
                .roomType(request.getRoomType())
                .cinemaId(request.getCinemaId())
                .status(RoomStatus.ACTIVE)
                .build();
    }
}