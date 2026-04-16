package com.cinemaebooking.backend.cinema.domain.model;

import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.common.domain.BaseEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

/**
 * Cinema - Aggregate Root representing a movie theater.
 * Responsibility:
 * - Core business attributes and state
 * - Enforce cinema business rules and invariants
 * - Provide state transition methods
 * This is a rich (non-anemic) domain model.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@SuperBuilder(toBuilder = true)
public class Cinema extends BaseEntity<CinemaId> {

    private final String name;
    private final String address;
    private final String city;
    private final CinemaStatus status;

    /**
     * Business invariants validation
     */
    public void validate() {
        Objects.requireNonNull(name, "Cinema name cannot be null");
        Objects.requireNonNull(address, "Cinema address cannot be null");
        Objects.requireNonNull(city, "City cannot be null");

        if (name.trim().isEmpty()) {
            throw new IllegalStateException("Cinema name cannot be empty");
        }
    }

    public boolean isActive() {
        return this.status == CinemaStatus.ACTIVE;
    }

    public Cinema activate() {
        if (this.status == CinemaStatus.ACTIVE) {
            return this; // idempotent
        }
        return this.toBuilder().status(CinemaStatus.ACTIVE).build();
    }

    public Cinema deactivate() {
        if (this.status == CinemaStatus.INACTIVE) {
            return this;
        }
        return this.toBuilder().status(CinemaStatus.INACTIVE).build();
    }

    public Cinema changeName(String newName) {
        return this.toBuilder().name(newName).build();
    }

    public Cinema relocate(String newAddress, String newCity) {
        return this.toBuilder()
                .address(newAddress)
                .city(newCity)
                .build();
    }
}