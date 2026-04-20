package com.cinemaebooking.backend.seat.domain.valueObject.seatType;

import com.cinemaebooking.backend.common.domain.BaseId;

public class SeatTypeId extends BaseId {
    public SeatTypeId(Long value){
        super(value);
    }

    /**
     * Factory method - strict version
     */
    public static SeatTypeId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("SeatTypeId value must be positive number");
        }
        return new SeatTypeId(value);
    }

    /**
     * For mapping purpose when id may be null (e.g. new entity)
     */
    public static SeatTypeId ofNullable(Long value) {
        return value == null ? null : new SeatTypeId(value);
    }
}
