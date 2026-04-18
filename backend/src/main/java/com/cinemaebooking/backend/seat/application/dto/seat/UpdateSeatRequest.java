package com.cinemaebooking.backend.seat.application.dto.seat;

import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSeatRequest {
    @NotBlank(message = "RowLabel must be not blank")
    private String rowLabel;

    @NotNull(message = "ColumnNumber must be not null")
    @Min(value = 1, message = "ColumnNumber must be at least 1")
    private Integer columnNumber;

    @NotNull(message = "SeatType must be not null")
    private Long seatTypeId;

    @NotNull(message = "SeatStatus must be not null")
    private SeatStatus seatStatus;
}
