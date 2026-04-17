package com.cinemaebooking.backend.cinema.application.validator;

import com.cinemaebooking.backend.cinema.application.dto.UpdateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.common.exception.domain.CinemaExceptions;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * CinemaCommandValidator
 * Responsibility:
 * - Validate request structure using validation engine + profiles
 * - Validate business rules (uniqueness, constraints)
 *
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
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Cinema id and request must not be null");
        }
    }

    // ================== FIELD VALIDATION ==================

    private void validateFields(UpdateCinemaRequest request) {

        var profile = ValidationFactory.cinema();

        ValidationEngine.validate(
                request.getName(),
                "Cinema name",
                profile.nameRules()
        );

        ValidationEngine.validate(
                request.getAddress(),
                "Cinema address",
                profile.addressRules()
        );

        ValidationEngine.validate(
                request.getCity(),
                "City",
                profile.cityRules()
        );

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