package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.mapper.RoomResponseMapper;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomExceptions;
import com.cinemaebooking.backend.room_layout.application.port.roomLayout.RoomLayoutRepository;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * GetRoomByIdUseCase - Handles retrieving a room by id.
 */
@Service
@RequiredArgsConstructor
public class GetRoomByIdUseCase {

    private final RoomRepository roomRepository;
    private final RoomLayoutRepository layoutRepository;
    private final RoomResponseMapper mapper;

    public RoomResponse execute(RoomId id) {

        // ================== VALIDATION ==================
        if (id == null) {
            throw CommonExceptions.invalidInput("roomId", ErrorCategory.REQUIRED,"Room id must not be null");
        }

        // ================== LOAD ==================
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> RoomExceptions.notFound(id));
        RoomLayout currentLayout = layoutRepository.findCurrentByRoomIdAndDate(room.getId().getValue(), LocalDate.now())
                .orElse(null); // hoặc fallback
        return mapper.toRoomResponse(room, currentLayout);
    }
}