package com.cinemaebooking.backend.seat.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.SeatTypeExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.seat.application.dto.seatType.CreateSeatTypeRequest;
import com.cinemaebooking.backend.seat.application.dto.seatType.UpdateSeatTypeRequest;
import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatTypeCommandValidator {

    private final SeatTypeRepository seatTypeRepository;

    // ================== CREATE ==================

    public void validateCreateRequest(CreateSeatTypeRequest request) {

        validateCreateInput(request);

        validateFields(
                request.getName(),
                request.getBasePrice()
        );

        validateDuplicateForCreate(
                normalize(request.getName())
        );
    }

    // ================== UPDATE ==================

    public void validateUpdateRequest(SeatTypeId id, UpdateSeatTypeRequest request) {

        validateUpdateInput(id, request);

        validateFields(
                request.getName(),
                request.getBasePrice()
        );

        validateDuplicateForUpdate(
                id,
                normalize(request.getName())
        );
    }

    // ================== INPUT ==================

    private void validateCreateInput(CreateSeatTypeRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }
    }

    private void validateUpdateInput(SeatTypeId id, UpdateSeatTypeRequest request) {
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("SeatType id and request must not be null");
        }
    }

    // ================== FIELD ==================

    private void validateFields(String name, Long basePrice) {

        var profile = ValidationFactory.seatType();

        ValidationEngine.of()
                .validate(name, "Seat type name", profile.nameRules())
                .validate(basePrice, "Base price", profile.basePriceRules())
                .throwIfInvalid();
    }

    // ================== BUSINESS ==================

    private void validateDuplicateForCreate(String name) {

        if (name != null) {
            boolean exists = seatTypeRepository.existsByName(name);

            if (exists) {
                throw SeatTypeExceptions.duplicateSeatTypeName(name);
            }
        }
    }

    private void validateDuplicateForUpdate(SeatTypeId id, String name) {

        if (name != null) {
            boolean exists = seatTypeRepository.existsByNameAndIdNot(name, id);

            if (exists) {
                throw SeatTypeExceptions.duplicateSeatTypeName(name);
            }
        }
    }

    // ================== NORMALIZE ==================

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
