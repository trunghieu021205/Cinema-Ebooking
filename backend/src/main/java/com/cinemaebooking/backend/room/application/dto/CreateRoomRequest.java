package com.cinemaebooking.backend.room.application.dto;

import com.cinemaebooking.backend.room.domain.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateRoomRequest {

    @NotBlank(message = "Room name must not be blank")
    private String name;

    @NotNull(message = "Total seats must not be null")
    @Min(value = 1, message = "Total seats must be at least 1")
    private Integer totalSeats;

    @NotNull(message = "Room type must not be null")
    private RoomType roomType;

    @NotNull(message = "Cinena id must not be null")
    private Long cinemaId;
}
