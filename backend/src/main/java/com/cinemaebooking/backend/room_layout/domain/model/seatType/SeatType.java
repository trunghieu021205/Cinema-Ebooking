package com.cinemaebooking.backend.room_layout.domain.model.seatType;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.room_layout.domain.valueObject.seatType.SeatTypeId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@SuperBuilder(toBuilder = true)
public class SeatType extends BaseEntity<SeatTypeId> {

    private String name;
    private BigDecimal basePrice;

    // ================== BUSINESS METHODS ==================

    public void update(String name, BigDecimal basePrice) {
        validate(name, basePrice);

        this.name = name;
        this.basePrice = basePrice;
    }

    // ================== VALIDATION ==================

    private void validate(String name, BigDecimal basePrice) {

        if (name == null || name.trim().isEmpty()) {
            throw CommonExceptions.invalidInput("Seat type name must not be empty");
        }

        if (basePrice == null ) {
            throw CommonExceptions.invalidInput("Base price must be positive");
        }
    }
}