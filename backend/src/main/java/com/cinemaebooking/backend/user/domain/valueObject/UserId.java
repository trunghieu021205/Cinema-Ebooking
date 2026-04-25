package com.cinemaebooking.backend.user.domain.valueObject;

import com.cinemaebooking.backend.common.domain.BaseId;

public class UserId extends BaseId {
    public UserId(Long value ){super (value);}

    /**
     * Factory method - strict version
     */
    public static UserId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("User id must be positive number");
        }
        return new UserId(value);
    }

    /**
     * For mapping purpose when id may be null (e.g. new entity)
     */
    public static UserId ofNullable(Long value) {
        return value == null ? null : new UserId(value);
    }
}
