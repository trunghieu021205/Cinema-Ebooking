package com.cinemaebooking.backend.seat.application.dto.seat;

import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SeatResponse {
    Long id;
    String rowLabel;
    Integer columnNumber;
    SeatStatus status;
    Long seatTypeId;
    Long roomId;

}
