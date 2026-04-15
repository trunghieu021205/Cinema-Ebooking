package com.cinemaebooking.backend.room.application.dto;

import com.cinemaebooking.backend.room.domain.enums.RoomStatus;
import com.cinemaebooking.backend.room.domain.enums.RoomType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String name;
    private Integer totalSeats;
    private RoomType roomType;
    private RoomStatus status;
    private Long cinemaId;
}
