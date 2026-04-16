package com.cinemaebooking.backend.cinema.application.validator;

import com.cinemaebooking.backend.cinema.application.dto.UpdateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.common.exception.domain.CinemaExceptions;
import com.cinemaebooking.backend.common.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * CinemaCommandValidator
 * Responsibility:
 * - Validate request structure (using ValidationUtils)
 * - Validate business rules (uniqueness, constraints)
 * @author Hieu Nguyen
 * @since 2026
 */
@Component
@RequiredArgsConstructor
public class CinemaCommandValidator {

    private final CinemaRepository cinemaRepository;

    public void validateUpdateRequest(CinemaId id, UpdateCinemaRequest request) {

        // ================== BASIC VALIDATION ==================
        validateBasicInput(id, request);

        // ================== FIELD VALIDATION ==================
        validateFields(request);

        // ================== BUSINESS VALIDATION ==================
        validateBusinessRules(id, request);
    }

    // ================== BASIC INPUT ==================

    private void validateBasicInput(CinemaId id, UpdateCinemaRequest request) {
        ValidationUtils.requireNonNull(id, "Cinema id");
        ValidationUtils.requireNonNull(request, "UpdateCinemaRequest");
    }

    // ================== FIELD VALIDATION ==================

    private void validateFields(UpdateCinemaRequest request) {

        // name
        ValidationUtils.validateString(request.getName(), "Cinema name", 3, 80);
        ValidationUtils.requireNotNumericOnly(request.getName(),  "Cinema name");
        // address
        ValidationUtils.validateString(request.getAddress(), "Cinema address", 5, 150);
        ValidationUtils.requireNotNumericOnly(request.getAddress(), "Cinema address");

        // city
        ValidationUtils.validateString(request.getCity(), "City", 2, 50);
        ValidationUtils.requireNotNumericOnly(request.getCity(), "City");

        // status
        ValidationUtils.requireNonNullEnum(request.getStatus(), "Cinema status");
    }

    // ================== BUSINESS VALIDATION ==================

    private void validateBusinessRules(CinemaId id, UpdateCinemaRequest request) {
        validateDuplicateName(id, request.getName());
        validateDuplicateLocation(id, request.getAddress(), request.getCity());
    }

    private void validateDuplicateName(CinemaId id, String name) {
        cinemaRepository.findByName(name)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw CinemaExceptions.alreadyExists(
                                "Cinema already exists with name: " + name
                        );
                    }
                });
    }

    private void validateDuplicateLocation(CinemaId id, String address, String city) {
        boolean exists = cinemaRepository.existsByAddressAndCityAndIdNot(
                address,
                city,
                id
        );

        if (exists) {
            throw CinemaExceptions.alreadyExists(
                    "Cinema already exists at address: " + address + ", city: " + city
            );
        }
    }
}