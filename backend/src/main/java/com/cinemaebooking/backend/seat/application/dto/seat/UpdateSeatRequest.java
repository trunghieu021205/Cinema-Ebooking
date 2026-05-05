package com.cinemaebooking.backend.seat.application.dto.seat;

import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSeatRequest {
    private Long seatTypeId;
    private SeatStatus seatStatus;
}
