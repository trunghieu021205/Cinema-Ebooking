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

        // ================== BASIC ==================
        validateBasicInput(id, request);

        // ================== FIELD ==================
        validateFields(request);

        // ================== BUSINESS ==================
        validateBusinessRules(id, request);
    }

    // ================== BASIC ==================

    private void validateBasicInput(CinemaId id, UpdateCinemaRequest request) {
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Cinema id and request must not be null");
        }
    }

    // ================== FIELD ==================

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

    // ================== BUSINESS ==================

    private void validateBusinessRules(CinemaId id, UpdateCinemaRequest request) {

        String name = normalize(request.getName());
        String address = normalize(request.getAddress());
        String city = normalize(request.getCity());

        // guard null → chỉ check khi có value
        if (name != null) {
            validateDuplicateName(id, name);
        }

        if (address != null && city != null) {
            validateDuplicateLocation(id, address, city);
        }
    }

    private void validateDuplicateName(CinemaId id, String name) {

        cinemaRepository.findByNameIgnoreCase(name)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw CinemaExceptions.duplicateCinemaName(name);
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
            throw CinemaExceptions.duplicateCinemaLocation(address, city);
        }
    }

    // ================== UTILS ==================

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}