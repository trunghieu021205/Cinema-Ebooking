package com.cinemaebooking.backend.room.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.room.domain.enums.RoomStatus;
import com.cinemaebooking.backend.room.domain.enums.RoomType;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)

public class Room extends BaseEntity<RoomId> {
    private String name;
    private Integer totalSeats;
    private RoomType roomType;
    private RoomStatus status;
    private Long cinemaId;

}

