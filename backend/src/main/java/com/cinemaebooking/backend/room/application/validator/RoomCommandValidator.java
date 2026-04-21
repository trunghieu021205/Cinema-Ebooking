package com.cinemaebooking.backend.room.application.validator;


import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.room.application.dto.CreateRoomRequest;
import com.cinemaebooking.backend.room.application.dto.UpdateRoomRequest;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * RoomCommandValidator
 * Responsibility:
 * - Validate input structure (create/update)
 * - Validate domain business rules (uniqueness)
 */
@Component
@RequiredArgsConstructor
public class RoomCommandValidator {

    private final RoomRepository roomRepository;

    // ================== CREATE ==================

    public void validateCreateRequest(CreateRoomRequest request) {

        validateCreateInput(request);

        validateFields(
                request.getName(),
                request.getTotalSeats(),
                request.getRoomType(),
                request.getCinemaId()
        );

        validateDuplicateForCreate(
                normalize(request.getName()),
                request.getCinemaId()
        );
    }

    // ================== UPDATE ==================

    public void validateUpdateRequest(RoomId id, UpdateRoomRequest request) {

        validateUpdateInput(id, request);

        validateFields(
                request.getName(),
                request.getTotalSeats(),
                request.getRoomType(),
                null // update không cần cinemaId
        );

        validateDuplicateForUpdate(
                id,
                normalize(request.getName())
        );
    }

    // ================== INPUT VALIDATION ==================

    private void validateCreateInput(CreateRoomRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }
    }

    private void validateUpdateInput(RoomId id, UpdateRoomRequest request) {
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Room id and request must not be null");
        }
    }

    // ================== FIELD VALIDATION ==================

    private void validateFields(
            String name,
            Integer totalSeats,
            com.cinemaebooking.backend.room.domain.enums.RoomType roomType,
            Long cinemaId
    ) {

        var profile = ValidationFactory.room(); // bạn cần tạo profile này

        ValidationEngine.validate(name, "Room name", profile.nameRules());
        ValidationEngine.validate(totalSeats, "Total seats", profile.capacityRules());
        ValidationEngine.validate(roomType, "Room type", profile.typeRules());

        if (cinemaId != null) {
            ValidationEngine.validate(cinemaId, "Cinema id", profile.cinemaIdRules());
        }
    }

    // ================== BUSINESS - CREATE ==================

    private void validateDuplicateForCreate(String name, Long cinemaId) {

        if (name != null && cinemaId != null) {
            boolean exists = roomRepository.existsByNameAndCinemaId(name, cinemaId);

            if (exists) {
                throw RoomExceptions.duplicateRoomInCinema(name, cinemaId);
            }
        }
    }

    // ================== BUSINESS - UPDATE ==================

    private void validateDuplicateForUpdate(RoomId id, String name) {

        if (name == null) return;

        // Lấy room từ DB
        var room = roomRepository.findById(id)
                .orElseThrow(() -> RoomExceptions.notFound(id));

        Long cinemaId = room.getCinemaId();

        boolean exists = roomRepository.existsByNameAndCinemaIdAndIdNot(name, cinemaId, id);

        if (exists) {
            throw RoomExceptions.duplicateRoomInCinema(name, cinemaId);
        }
    }

    // ================== NORMALIZE ==================

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}