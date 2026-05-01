package com.cinemaebooking.backend.showtime.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

public final class ShowtimeFormatId extends BaseId {

    public ShowtimeFormatId(Long value) {
        super(value);
    }

    public static ShowtimeFormatId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ShowtimeFormatId must be positive");
        }
        return new ShowtimeFormatId(value);
    }

    public static ShowtimeFormatId ofNullable(Long value) {
        return value == null ? null : new ShowtimeFormatId(value);
    }
}