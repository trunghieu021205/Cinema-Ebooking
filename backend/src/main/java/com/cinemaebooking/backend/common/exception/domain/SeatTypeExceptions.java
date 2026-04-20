package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * SeatTypeExceptions - Domain-specific exceptions for SeatType.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeatTypeExceptions {

    // ================== NOT FOUND ==================

    public static BaseException notFound(SeatTypeId id) {
        return CommonExceptions.resourceNotFound(
                "Seat type not found with id: " + id
        );
    }

    // ================== DUPLICATE ==================

    public static BaseException duplicateSeatTypeName(String name) {
        return CommonExceptions.resourceAlreadyExists(
                "Seat type name already exists: " + name
        );
    }

    // ================== BUSINESS RULE ==================

    public static BaseException invalidBasePrice(Long price) {
        return CommonExceptions.invalidInput(
                "Invalid base price: " + price
        );
    }
}