package com.cinemaebooking.backend.showtime.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

public final class ShowtimeId extends BaseId {

    public ShowtimeId(Long value) {
        super(value);
    }

    public static ShowtimeId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ShowtimeId value must be positive number");
        }
        return new ShowtimeId(value);
    }

    public static ShowtimeId ofNullable(Long value) {
        return value == null ? null : new ShowtimeId(value);
    }
}