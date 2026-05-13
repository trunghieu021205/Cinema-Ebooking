package com.cinemaebooking.backend.showtime_seat.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

public class ShowtimeSeatId extends BaseId {
    public ShowtimeSeatId(Long value){super(value);}


    public static ShowtimeSeatId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ShowtimeSeatId value must be positive number");
        }
        return new ShowtimeSeatId(value);
    }

    /**
     * For mapping purpose when id may be null (e.g. new entity)
     */
    public static ShowtimeSeatId ofNullable(Long value) {
        return value == null ? null : new ShowtimeSeatId(value);
    }
}
