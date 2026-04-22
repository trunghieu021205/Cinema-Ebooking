package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.mapper.RoomResponseMapper;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * GetRoomListUseCase - Handles retrieving paginated room list.
 */
@Service
@RequiredArgsConstructor
public class GetRoomListUseCase {

    private final RoomRepository roomRepository;
    private final RoomResponseMapper mapper;

    public Page<RoomResponse> execute(Pageable pageable) {

        // ================== VALIDATION ==================
        if (pageable == null) {
            throw CommonExceptions.invalidInput("Pageable must not be null");
        }

        // ================== BUSINESS ==================
        return roomRepository.findAll(pageable)
                .map(mapper::toRoomResponse);
    }
}