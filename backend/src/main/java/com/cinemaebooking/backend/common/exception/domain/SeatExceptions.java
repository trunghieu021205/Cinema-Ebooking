package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * SeatExceptions - Domain-specific exceptions for Seat.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeatExceptions {

    // ================== NOT FOUND ==================

    public static BaseException notFound(SeatId id) {
        return CommonExceptions.resourceNotFound(
                "Seat not found with id: " + id
        );
    }

    // ================== DUPLICATE ==================

    public static BaseException duplicateSeatPosition(String rowLabel, Integer columnNumber, Long roomId) {
        return CommonExceptions.resourceAlreadyExists(
                "Seat already exists at row: " + rowLabel +
                        ", column: " + columnNumber +
                        " in room: " + roomId
        );
    }

    // ================== BUSINESS RULE ==================

    public static BaseException invalidSeatPosition(String rowLabel, Integer columnNumber) {
        return CommonExceptions.invalidInput(
                "Invalid seat position: row=" + rowLabel + ", column=" + columnNumber
        );
    }

    public static BaseException inactiveSeat(SeatId id) {
        return CommonExceptions.invalidInput(
                "Seat is inactive: " + id
        );
    }
}