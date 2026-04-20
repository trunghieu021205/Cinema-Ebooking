package com.cinemaebooking.backend.seat.domain.model.seatType;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class SeatType extends BaseEntity<SeatTypeId> {

    private String name;
    private Long basePrice;

    // ================== BUSINESS METHODS ==================

    public void update(String name, Long basePrice) {
        validate(name, basePrice);

        this.name = name;
        this.basePrice = basePrice;
    }

    // ================== VALIDATION ==================

    private void validate(String name, Long basePrice) {

        if (name == null || name.trim().isEmpty()) {
            throw CommonExceptions.invalidInput("Seat type name must not be empty");
        }

        if (basePrice == null || basePrice <= 0) {
            throw CommonExceptions.invalidInput("Base price must be positive");
        }
    }
}