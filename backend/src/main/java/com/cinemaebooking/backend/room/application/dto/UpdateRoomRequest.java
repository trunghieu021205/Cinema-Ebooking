package com.cinemaebooking.backend.room.application.dto;

import com.cinemaebooking.backend.room.domain.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoomRequest {
    private String name;
    private RoomStatus status;
}
