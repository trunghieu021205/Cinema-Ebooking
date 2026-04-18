package com.cinemaebooking.backend.seat.domain.model.seat;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class Seat extends BaseEntity<SeatId> {

    private String rowLabel;
    private Integer columnNumber;
    private SeatStatus status;
    private Long seatTypeId;
    private Long roomId;
}

