package com.cinemaebooking.backend.room.application.dto;

import com.cinemaebooking.backend.room.domain.enums.RoomStatus;
import com.cinemaebooking.backend.room.domain.enums.RoomType;
import lombok.*;

@Getter
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String name;
    private RoomType roomType;
    private Integer numberOfRows;
    private Integer numberOfCols;
    private Integer totalSeats;
    private RoomStatus status;
    private Long cinemaId;
}
