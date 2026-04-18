package com.cinemaebooking.backend.seat.domain.model.seatType;


import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class SeatType extends BaseEntity<SeatTypeId> {

    private String name;
    private Long basePrice;
}