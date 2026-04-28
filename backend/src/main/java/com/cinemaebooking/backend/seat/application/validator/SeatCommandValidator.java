package com.cinemaebooking.backend.seat.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.SeatExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.seat.application.dto.seat.CreateSeatRequest;
import com.cinemaebooking.backend.seat.application.dto.seat.UpdateSeatRequest;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatCommandValidator {

    private final SeatRepository seatRepository;

    // ================== CREATE ==================

    public void validateCreateRequest(CreateSeatRequest request) {

        validateCreateInput(request);

        validateFields(
                request.getRowLabel(),
                request.getColumnNumber(),
                null,
                request.getSeatTypeId(),
                request.getRoomId()
        );

        validateDuplicateForCreate(
                normalize(request.getRowLabel()),
                request.getColumnNumber(),
                request.getRoomId()
        );
    }

    // ================== UPDATE ==================

    public void validateUpdateRequest(SeatId id, UpdateSeatRequest request) {

        validateUpdateInput(id, request);

        Seat existingSeat = seatRepository.findById(id)
                .orElseThrow(() -> SeatExceptions.notFound(id));

        Long roomId = existingSeat.getRoomId();

        validateFields(
                request.getRowLabel(),
                request.getColumnNumber(),
                request.getSeatStatus(),
                request.getSeatTypeId(),
                null
        );

        validateDuplicateForUpdate(
                id,
                roomId,
                normalize(request.getRowLabel()),
                request.getColumnNumber()
        );
    }

    // ================== INPUT ==================

    private void validateCreateInput(CreateSeatRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }
    }

    private void validateUpdateInput(SeatId id, UpdateSeatRequest request) {
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Seat id and request must not be null");
        }
    }

    // ================== FIELD VALIDATION ==================

    private void validateFields(
            String rowLabel,
            Integer columnNumber,
            SeatStatus status,
            Long seatTypeId,
            Long roomId
    ) {

        var profile = ValidationFactory.seat();

        ValidationEngine.of()
                .validate(rowLabel, "rowLabel", profile.rowLabelRules())
                .validate(columnNumber, "columnNumber", profile.columnNumberRules())
                .validate(status, "status", profile.statusRules())
                .validate(seatTypeId, "seatTypeId", profile.seatTypeIdRules())
                .validate(roomId, "roomId", profile.roomIdRules())
                .throwIfInvalid();

    }

    // ================== BUSINESS - CREATE ==================

    private void validateDuplicateForCreate(
            String rowLabel,
            Integer columnNumber,
            Long roomId
    ) {

        if (rowLabel == null || columnNumber == null || roomId == null) return;

        boolean exists = seatRepository
                .existsByRoomIdAndRowLabelAndColumnNumber(
                        roomId,
                        rowLabel,
                        columnNumber
                );

        if (exists) {
            throw SeatExceptions.duplicateSeatPosition(rowLabel, columnNumber, roomId);
        }
    }

    // ================== BUSINESS - UPDATE ==================

    private void validateDuplicateForUpdate(
            SeatId id,
            Long roomId,
            String rowLabel,
            Integer columnNumber
    ) {

        if (id == null || roomId == null || rowLabel == null || columnNumber == null) return;

        boolean exists = seatRepository
                .existsByRoomIdAndRowLabelAndColumnNumberAndIdNot(
                        roomId,
                        rowLabel,
                        columnNumber,
                        id
                );

        if (exists) {
            throw SeatExceptions.duplicateSeatPosition(rowLabel, columnNumber, roomId);
        }
    }

    // ================== NORMALIZE ==================

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}