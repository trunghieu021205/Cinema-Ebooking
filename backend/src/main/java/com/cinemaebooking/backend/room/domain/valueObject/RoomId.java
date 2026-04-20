package com.cinemaebooking.backend.room.domain.valueObject;

import com.cinemaebooking.backend.common.domain.BaseId;

public class RoomId extends BaseId {
    public RoomId(Long value ){super (value);}

    /**
     * Factory method - strict version
     */
    public static RoomId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("RoomId value must be positive number");
        }
        return new RoomId(value);
    }

    /**
     * For mapping purpose when id may be null (e.g. new entity)
     */
    public static RoomId ofNullable(Long value) {
        return value == null ? null : new RoomId(value);
    }
}
