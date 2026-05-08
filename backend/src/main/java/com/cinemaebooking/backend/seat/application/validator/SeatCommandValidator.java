package com.cinemaebooking.backend.seat.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.SeatExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.seat.application.dto.seat.CreateSeatRequest;
import com.cinemaebooking.backend.seat.application.dto.seat.UpdateSeatRequest;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * SeatCommandValidator
 * Responsibility:
 * - Validate input structure (create/update)
 * - Validate domain business rules (uniqueness)
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Component
@RequiredArgsConstructor
public class SeatCommandValidator {

    private final SeatRepository seatRepository;

    // ================== PUBLIC API ==================

    public void validateCreateRequest(CreateSeatRequest request) {
        validateRequest(request, null, false);
    }

    public void validateUpdateRequest(SeatId id, UpdateSeatRequest request) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Seat id and request must not be null");
        }
        validateRequest(request, id, true);
    }

    // ================== CORE VALIDATION ==================

    private void validateRequest(Object requestObj, SeatId id, boolean isUpdate) {
        if (requestObj == null) {
            throw CommonExceptions.invalidInput(isUpdate
                    ? "Seat id and request must not be null"
                    : "Create seat request must not be null");
        }

        Integer rowIndex  = extractRowIndex(requestObj);
        Integer colIndex  = extractColIndex(requestObj);
        String label      = extractLabel(requestObj);
        Long seatTypeId   = extractSeatTypeId(requestObj);
        SeatStatus status = extractStatus(requestObj);
        Long roomId       = extractRoomId(requestObj);

        // ================== PHASE 1: FORMAT VALIDATION ==================
        ValidationEngine engine = ValidationEngine.of();

        if (!isUpdate) {
            engine
                    .validate(rowIndex, "rowIndex", ValidationFactory.seat().rowIndexRules())
                    .validate(colIndex, "colIndex", ValidationFactory.seat().colIndexRules())
                    .validate(label, "label", ValidationFactory.seat().labelRules())
                    .validate(roomId, "roomId", ValidationFactory.seat().roomIdRules());
        }

        if (isUpdate) {
            engine
                    .validate(seatTypeId, "seatTypeId", ValidationFactory.seat().seatTypeIdRules())
                    .validate(status, "status", ValidationFactory.seat().statusRules());
        }

        if (engine.hasErrors()) {
            engine.throwIfInvalid();
            return;
        }

        // ================== PHASE 2: BUSINESS VALIDATION ==================
        if (!isUpdate) {
            engine.validateUnique(
                    rowIndex + "," + colIndex,
                    "rowIndex",
                    value -> seatRepository.existsByRoomIdAndRowIndexAndColIndex(roomId, rowIndex, colIndex)
            );
        }

        engine.throwIfInvalid();
    }

    // ================== EXTRACTORS ==================

    private Integer extractRowIndex(Object request) {
        if (request instanceof CreateSeatRequest req) return req.getRowIndex();
        return null;
    }

    private Integer extractColIndex(Object request) {
        if (request instanceof CreateSeatRequest req) return req.getColIndex();
        return null;
    }

    private String extractLabel(Object request) {
        if (request instanceof CreateSeatRequest req) return req.getLabel();
        return null;
    }

    private Long extractSeatTypeId(Object request) {
        if (request instanceof CreateSeatRequest req) return req.getSeatTypeId();
        if (request instanceof UpdateSeatRequest req) return req.getSeatTypeId();
        return null;
    }

    private SeatStatus extractStatus(Object request) {
        if (request instanceof UpdateSeatRequest req) return req.getSeatStatus();
        return null;
    }

    private Long extractRoomId(Object request) {
        if (request instanceof CreateSeatRequest req) return req.getRoomId();
        return null;
    }
}