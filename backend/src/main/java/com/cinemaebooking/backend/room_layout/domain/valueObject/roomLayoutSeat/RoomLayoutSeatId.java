package com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayoutSeat;

import com.cinemaebooking.backend.common.domain.BaseId;

public class RoomLayoutSeatId extends BaseId {
    public RoomLayoutSeatId(Long value){
        super((value));
    }

    /**
     * Factory method - strict version
     */
    public static RoomLayoutSeatId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("RoomLayoutSeatId value must be positive number");
        }
        return new RoomLayoutSeatId(value);
    }

    /**
     * For mapping purpose when id may be null (e.g. new entity)
     */
    public static RoomLayoutSeatId ofNullable(Long value) {
        return value == null ? null : new RoomLayoutSeatId(value);
    }
}
