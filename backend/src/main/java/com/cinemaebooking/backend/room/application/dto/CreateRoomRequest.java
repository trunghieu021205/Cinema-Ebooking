package com.cinemaebooking.backend.room.application.dto;

import com.cinemaebooking.backend.room.domain.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {
    private String name;
    private RoomType roomType;
    private Integer numberOfRows;
    private Integer numberOfCols;
    private Long cinemaId;
}
