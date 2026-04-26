package com.cinemaebooking.backend.combo.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

public final class ComboId extends BaseId {

    private ComboId(Long value) {
        super(value);
    }

    public static ComboId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ComboId value must be positive number");
        }
        return new ComboId(value);
    }

    public static ComboId ofNullable(Long value) {
        return value == null ? null : new ComboId(value);
    }
}