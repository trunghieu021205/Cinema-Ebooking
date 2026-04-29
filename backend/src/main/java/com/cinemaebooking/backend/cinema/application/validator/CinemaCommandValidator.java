package com.cinemaebooking.backend.cinema.application.validator;

import com.cinemaebooking.backend.cinema.application.dto.CreateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.dto.UpdateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
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

    // ================== PUBLIC API ==================

    public void validateCreateRequest(CreateCinemaRequest request) {
        validateRequest(request, null, false);
    }

    public void validateUpdateRequest(CinemaId id, UpdateCinemaRequest request) {
        validateRequest(request, id, true);
    }

    // ================== CORE VALIDATION ==================

    private void validateRequest(Object requestObj, CinemaId id, boolean isUpdate) {
        if (requestObj == null) {
            throw CommonExceptions.invalidInput(isUpdate
                    ? "Cinema id and request must not be null"
                    : "Create cinema request must not be null");
        }

        String name = extractName(requestObj);
        String address = extractAddress(requestObj);
        String city = extractCity(requestObj);
        CinemaStatus status = extractStatus(requestObj);

        // ================== PHASE 1: FORMAT VALIDATION ==================
        ValidationEngine engine = ValidationEngine.of()
                .validate(name, "name", ValidationFactory.cinema().nameRules())
                .validate(address, "address", ValidationFactory.cinema().addressRules())
                .validate(city, "city", ValidationFactory.cinema().cityRules());

        if (isUpdate) {
            engine.validate(status, "status", ValidationFactory.cinema().statusRules());
        }

        if (engine.hasErrors()) {
            engine.throwIfInvalid();
            return;
        }

        // ================== PHASE 2: BUSINESS VALIDATION ==================
        String normalizedName = normalize(name);
        String normalizedAddress = normalize(address);
        String normalizedCity = normalize(city);

        engine
                .validateUnique(normalizedName, "name",
                        value -> nameExists(value, id, isUpdate))
                .validateUnique(normalizedAddress, "address",
                        value -> locationExists(value, normalizedCity, id, isUpdate))
                .throwIfInvalid();
    }

    // ================== BUSINESS CHECKS ==================

    private boolean nameExists(String name, CinemaId id, boolean isUpdate) {
        if (name == null) return false;
        return isUpdate
                ? cinemaRepository.existsByNameAndIdNot(name, id)
                : cinemaRepository.existsByName(name);
    }

    private boolean locationExists(String address, String city, CinemaId id, boolean isUpdate) {
        if (address == null || city == null) return false;
        return isUpdate
                ? cinemaRepository.existsByAddressAndCityAndIdNot(address, city, id)
                : cinemaRepository.existsByAddressAndCity(address, city);
    }

    // ================== EXTRACTORS ==================

    private String extractName(Object request) {
        if (request instanceof CreateCinemaRequest req) return req.getName();
        if (request instanceof UpdateCinemaRequest req) return req.getName();
        return null;
    }

    private String extractAddress(Object request) {
        if (request instanceof CreateCinemaRequest req) return req.getAddress();
        if (request instanceof UpdateCinemaRequest req) return req.getAddress();
        return null;
    }

    private String extractCity(Object request) {
        if (request instanceof CreateCinemaRequest req) return req.getCity();
        if (request instanceof UpdateCinemaRequest req) return req.getCity();
        return null;
    }

    private CinemaStatus extractStatus(Object request) {
        if (request instanceof UpdateCinemaRequest req) return req.getStatus();
        return null;
    }

    // ================== HELPER ==================

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}