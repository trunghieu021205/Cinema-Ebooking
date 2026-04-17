package com.cinemaebooking.backend.cinema.domain.model;

import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Cinema - Aggregate Root representing a movie theater.
 * Responsibility:
 * - Maintain core state
 * - Enforce business invariants
 * - Provide behavior for state changes
 * Rich domain model (DDD)
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@SuperBuilder
public class Cinema extends BaseEntity<CinemaId> {

    private String name;
    private String address;
    private String city;
    private CinemaStatus status;

    // ================== BUSINESS METHODS ==================

    public void update(String name, String address, String city, CinemaStatus status) {
        validateBasicInfo(name, address, city);
        validateStatus(status);

        this.name = name;
        this.address = address;
        this.city = city;
        this.status = status;
    }

    public void updateInfo(String name, String address, String city) {
        validateBasicInfo(name, address, city);

        this.name = name;
        this.address = address;
        this.city = city;
    }

    public void changeStatus(CinemaStatus status) {
        validateStatus(status);
        this.status = status;
    }

    public void activate() {
        this.status = CinemaStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = CinemaStatus.INACTIVE;
    }

    public boolean isActive() {
        return this.status == CinemaStatus.ACTIVE;
    }

    // ================== VALIDATION ==================

    private void validateBasicInfo(String name, String address, String city) {
        if (name == null || name.trim().isEmpty()) {
            throw CommonExceptions.invalidInput("Cinema name must not be empty");
        }

        if (address == null || address.trim().isEmpty()) {
            throw CommonExceptions.invalidInput("Cinema address must not be empty");
        }

        if (city == null || city.trim().isEmpty()) {
            throw CommonExceptions.invalidInput("City must not be empty");
        }
    }

    private void validateStatus(CinemaStatus status) {
        if (status == null) {
            throw CommonExceptions.invalidInput("Cinema status must not be null");
        }
    }
}