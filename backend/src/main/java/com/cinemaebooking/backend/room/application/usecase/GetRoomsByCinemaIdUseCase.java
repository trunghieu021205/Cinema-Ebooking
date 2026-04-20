package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.mapper.RoomResponseMapper;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * GetRoomsByCinemaIdUseCase - Handles retrieving rooms by cinemaId (with pagination).
 */
@Service
@RequiredArgsConstructor
public class GetRoomsByCinemaIdUseCase {

    private final RoomRepository roomRepository;
    private final RoomResponseMapper mapper;

    public Page<RoomResponse> execute(Long cinemaId, Pageable pageable) {

        // ================== VALIDATION ==================
        if (cinemaId == null) {
            throw CommonExceptions.invalidInput("Cinema id must not be null");
        }

        if (pageable == null) {
            throw CommonExceptions.invalidInput("Pageable must not be null");
        }

        // ================== BUSINESS ==================
        Page<Room> rooms = roomRepository.findByCinemaId(cinemaId, pageable);

        // ================== MAP RESPONSE ==================
        return rooms.map(mapper::toRoomResponse);
    }
}