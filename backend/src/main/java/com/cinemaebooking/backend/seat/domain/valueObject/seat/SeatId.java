package com.cinemaebooking.backend.seat.domain.valueObject.seat;

import com.cinemaebooking.backend.common.domain.BaseId;

public class SeatId extends BaseId {
    public SeatId(Long value){
        super((value));
    }

    /**
     * Factory method - strict version
     */
    public static SeatId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("SeatId value must be positive number");
        }
        return new SeatId(value);
    }

    /**
     * For mapping purpose when id may be null (e.g. new entity)
     */
    public static SeatId ofNullable(Long value) {
        return value == null ? null : new SeatId(value);
    }
}
