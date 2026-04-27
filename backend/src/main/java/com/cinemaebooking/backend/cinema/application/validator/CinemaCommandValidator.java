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

        ValidationEngine.validate(name, "name", profile.nameRules());
        ValidationEngine.validate(address, "address", profile.addressRules());
        ValidationEngine.validate(city, "city", profile.cityRules());
    }

    // ================== BUSINESS - CREATE ==================

    private void validateDuplicateForCreate(String name, String address, String city) {

        if (name != null) {
            if (cinemaRepository.existsByName(name)) {
                throw CinemaExceptions.duplicateName(name);
            }
        }

        if (address != null && city != null) {
            boolean exists = cinemaRepository.existsByAddressAndCity(address, city);

            if (exists) {
                throw CinemaExceptions.duplicateLocation(address, city);
            }
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
            if (cinemaRepository.existsByNameAndIdNot(name, id)) {
                throw CinemaExceptions.duplicateName(name);
            }
        }

        if (address != null && city != null) {
            if (cinemaRepository.existsByAddressAndCityAndIdNot(address, city, id)) {
                throw CinemaExceptions.duplicateLocation(address, city);
            }
        }
    }


    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}