package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.mapper.RoomResponseMapper;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * GetRoomByIdUseCase - Handles retrieving a room by id.
 */
@Service
@RequiredArgsConstructor
public class GetRoomByIdUseCase {

    private final RoomRepository roomRepository;
    private final RoomResponseMapper mapper;

    public RoomResponse execute(RoomId id) {

        // ================== VALIDATION ==================
        if (id == null) {
            throw CommonExceptions.invalidInput("Room id must not be null");
        }

        // ================== LOAD ==================
        return roomRepository.findById(id)
                .map(mapper::toRoomResponse)
                .orElseThrow(() -> RoomExceptions.notFound(id));
    }
}