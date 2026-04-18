package com.cinemaebooking.backend.seat.application.dto.seat;

import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SeatResponse {
    Long id;
    String rowLabel;
    Integer columnNumber;
    SeatStatus status;
    Long seatTypeId;
    Long roomId;

}
