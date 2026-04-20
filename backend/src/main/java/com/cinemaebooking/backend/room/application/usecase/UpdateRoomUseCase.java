package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.RoomExceptions;
import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.dto.UpdateRoomRequest;
import com.cinemaebooking.backend.room.application.mapper.RoomResponseMapper;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.application.validator.RoomCommandValidator;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

/**
 * UpdateRoomUseCase - Handles updating an existing Room.
 * Flow:
 * 1. Validate input & business rules
 * 2. Load existing room
 * 3. Apply domain update
 * 4. Persist changes
 * 5. Map to response
 */
@Service
@RequiredArgsConstructor
public class UpdateRoomUseCase {

    private final RoomRepository roomRepository;
    private final RoomResponseMapper mapper;
    private final RoomCommandValidator validator;

    public RoomResponse execute(RoomId id, UpdateRoomRequest request) {

        // ================== VALIDATION ==================
        validator.validateUpdateRequest(id, request);

        // ================== LOAD ==================
        Room room = loadRoom(id);

        // ================== APPLY DOMAIN ==================
        applyUpdate(room, request);

        // ================== PERSIST ==================
        Room saved = persist(room);

        // ================== MAP RESPONSE ==================
        return mapper.toRoomResponse(saved);
    }

    // ================== PRIVATE METHODS ==================

    private Room loadRoom(RoomId id) {
        return roomRepository.findById(id)
                .orElseThrow(() ->
                        RoomExceptions.notFound(id)
                );
    }

    private void applyUpdate(Room room, UpdateRoomRequest request) {
        room.update(
                request.getName(),
                request.getTotalSeats(),
                request.getRoomType(),
                request.getStatus()
        );
    }

    private Room persist(Room room) {
        try {
            return roomRepository.update(room);
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
            throw CommonExceptions.concurrencyConflict();
        }
    }
}