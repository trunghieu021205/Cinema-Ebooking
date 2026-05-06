package com.cinemaebooking.backend.room_layout.application.mapper.seatType;

import com.cinemaebooking.backend.room_layout.application.dto.seatType.SeatTypeResponse;
import com.cinemaebooking.backend.room_layout.domain.model.seatType.SeatType;
import org.springframework.stereotype.Component;

@Component
public class SeatTypeResponseMapper {

    public SeatTypeResponse toResponse(SeatType seatType) {
        if (seatType == null) return null;

        return SeatTypeResponse.builder()
                .id(seatType.getId().getValue())
                .name(seatType.getName())
                .basePrice(seatType.getBasePrice())
                .build();
    }
}