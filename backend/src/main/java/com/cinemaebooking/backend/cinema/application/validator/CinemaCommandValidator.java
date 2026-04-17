package com.cinemaebooking.backend.cinema.application.validator;

import com.cinemaebooking.backend.cinema.application.dto.CreateCinemaRequest;
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
 * - Validate input structure (create/update)
 * - Validate domain business rules (uniqueness)
 * @author Hieu Nguyen
 * @since 2026
 */
@Component
@RequiredArgsConstructor
public class CinemaCommandValidator {

    private final CinemaRepository cinemaRepository;

    // ================== CREATE ==================

    public void validateCreateRequest(CreateCinemaRequest request) {

        validateCreateInput(request);

        validateFields(
                request.getName(),
                request.getAddress(),
                request.getCity()
        );

        validateDuplicateForCreate(
                normalize(request.getName()),
                normalize(request.getAddress()),
                normalize(request.getCity())
        );
    }

    // ================== UPDATE ==================

    public void validateUpdateRequest(CinemaId id, UpdateCinemaRequest request) {

        validateUpdateInput(id, request);

        validateFields(
                request.getName(),
                request.getAddress(),
                request.getCity()
        );

        validateDuplicateForUpdate(
                id,
                normalize(request.getName()),
                normalize(request.getAddress()),
                normalize(request.getCity())
        );
    }

    // ================== INPUT VALIDATION ==================

    private void validateCreateInput(CreateCinemaRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }
    }

    private void validateUpdateInput(CinemaId id, UpdateCinemaRequest request) {
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Cinema id and request must not be null");
        }
    }

    // ================== FIELD VALIDATION ==================

    private void validateFields(String name, String address, String city) {

        var profile = ValidationFactory.cinema();

        ValidationEngine.validate(name, "Cinema name", profile.nameRules());
        ValidationEngine.validate(address, "Cinema address", profile.addressRules());
        ValidationEngine.validate(city, "City", profile.cityRules());
    }

    // ================== BUSINESS - CREATE ==================

    private void validateDuplicateForCreate(String name, String address, String city) {

        if (name != null) {
            validateDuplicateName(null, name);
        }

        if (address != null && city != null) {
            validateDuplicateLocation(null, address, city);
        }
    }

    // ================== BUSINESS - UPDATE ==================

    private void validateDuplicateForUpdate(
            CinemaId id,
            String name,
            String address,
            String city
    ) {

        if (name != null) {
            validateDuplicateName(id, name);
        }

        if (address != null && city != null) {
            validateDuplicateLocation(id, address, city);
        }
    }

    // ================== DUPLICATE CHECK ==================

    private void validateDuplicateName(CinemaId id, String name) {

        cinemaRepository.findByNameIgnoreCase(name)
                .ifPresent(existing -> {

                    // create: id = null → always conflict if exists
                    // update: ignore same id
                    if (id == null || !existing.getId().equals(id)) {
                        throw CinemaExceptions.duplicateCinemaName(name);
                    }
                });
    }

    private void validateDuplicateLocation(
            CinemaId id,
            String address,
            String city
    ) {

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
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}