package com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayout;

import com.cinemaebooking.backend.common.domain.BaseId;

public class RoomLayoutId extends BaseId {
    public RoomLayoutId(Long value) {super(value);}

    public static RoomLayoutId of(Long value){
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("RoomLayoutId value must be positive number");
        }
        return new RoomLayoutId(value);
    }

    public static RoomLayoutId ofNullable(Long value){return value == null ? null : new RoomLayoutId(value);}
}
