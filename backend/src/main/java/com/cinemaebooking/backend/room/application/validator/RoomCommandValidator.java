package com.cinemaebooking.backend.room.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.room.application.dto.CreateRoomRequest;
import com.cinemaebooking.backend.room.application.dto.UpdateRoomRequest;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.enums.RoomStatus;
import com.cinemaebooking.backend.room.domain.enums.RoomType;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * RoomCommandValidator
 * Responsibility:
 * - Validate input structure (create/update)
 * - Validate domain business rules (uniqueness)
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Component
@RequiredArgsConstructor
public class RoomCommandValidator {

    private final RoomRepository roomRepository;

    // ================== PUBLIC API ==================

    public void validateCreateRequest(CreateRoomRequest request) {
        validateRequest(request, null, false);
    }

    public void validateUpdateRequest(RoomId id, UpdateRoomRequest request) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Room id and request must not be null");
        }
        validateRequest(request, id, true);
    }

    // ================== CORE VALIDATION ==================

    private void validateRequest(Object requestObj, RoomId id, boolean isUpdate) {
        if (requestObj == null) {
            throw CommonExceptions.invalidInput(isUpdate
                    ? "Room id and request must not be null"
                    : "Create room request must not be null");
        }

        String name = extractName(requestObj);
        Integer numberOfRows = extractNumberOfRows(requestObj);
        Integer numberOfCols = extractNumberOfCols(requestObj);
        RoomType roomType = extractRoomType(requestObj);
        RoomStatus status = extractStatus(requestObj);
        Long cinemaId     = extractCinemaId(requestObj);

        // ================== PHASE 1: FORMAT VALIDATION ==================
        ValidationEngine engine = ValidationEngine.of()
                .validate(name, "name", ValidationFactory.room().nameRules())
                .validate(roomType, "roomType", ValidationFactory.room().typeRules());

        if (!isUpdate) {
            engine
                    .validate(numberOfRows, "numberOfRows", ValidationFactory.room().numberOfRowsRules())
                    .validate(numberOfCols, "numberOfCols", ValidationFactory.room().numberOfColsRules())
                    .validate(cinemaId, "cinemaId", ValidationFactory.room().cinemaIdRules());
        }

        if (isUpdate) {
            engine.validate(status, "status", ValidationFactory.room().statusRules());
        }

        if (engine.hasErrors()) {
            engine.throwIfInvalid();
            return;
        }

        // ================== PHASE 2: BUSINESS VALIDATION ==================
        String normalizedName = normalize(name);

        if (!isUpdate) {
            engine.validateUnique(normalizedName, "name",
                    value -> roomRepository.existsByNameAndCinemaId(value, cinemaId));
        } else {
            engine.validateUnique(normalizedName, "name",
                    value -> roomRepository.existsByNameAndCinemaIdAndIdNot(value, resolveCinemaId(id), id));
        }

        engine.throwIfInvalid();
    }

    // ================== BUSINESS HELPERS ==================

    private Long resolveCinemaId(RoomId id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> RoomExceptions.notFound(id))
                .getCinemaId();
    }

    // ================== EXTRACTORS ==================

    private String extractName(Object request) {
        if (request instanceof CreateRoomRequest req) return req.getName();
        if (request instanceof UpdateRoomRequest req) return req.getName();
        return null;
    }

    private Integer extractNumberOfRows(Object request) {
        if (request instanceof CreateRoomRequest req) return req.getNumberOfRows();
        return null;
    }

    private Integer extractNumberOfCols(Object request) {
        if (request instanceof CreateRoomRequest req) return req.getNumberOfCols();
        return null;
    }

    private RoomType extractRoomType(Object request) {
        if (request instanceof CreateRoomRequest req) return req.getRoomType();
        if (request instanceof UpdateRoomRequest req) return req.getRoomType();
        return null;
    }

    private RoomStatus extractStatus(Object request) {
        if (request instanceof UpdateRoomRequest req) return req.getStatus();
        return null;
    }

    private Long extractCinemaId(Object request) {
        if (request instanceof CreateRoomRequest req) return req.getCinemaId();
        return null;
    }

    // ================== HELPER ==================

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}