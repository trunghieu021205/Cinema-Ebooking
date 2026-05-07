package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.room_layout.domain.valueObject.seatType.SeatTypeId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * SeatTypeExceptions - Domain-specific exceptions for SeatType.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeatTypeExceptions {

    // ================== NOT FOUND ==================

    public static BaseException notFound(SeatTypeId id) {
        return new BaseException(ErrorCode.SEAT_TYPE_NOT_FOUND,
                "Seat type not found with id: " + id
        );
    }

    // ================== DUPLICATE ==================

    public static BaseException duplicateSeatTypeName(String name) {
        return new BaseException(ErrorCode.SEAT_TYPE_ALREADY_EXISTS,
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