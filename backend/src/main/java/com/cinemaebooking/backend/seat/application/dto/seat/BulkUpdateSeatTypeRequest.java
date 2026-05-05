package com.cinemaebooking.backend.seat.application.dto.seat;

import java.util.List;

public record BulkUpdateSeatTypeRequest (
    List<Long> seatIds,
    Long seatTypeId
){}
