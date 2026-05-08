package com.cinemaebooking.backend.seat.application.dto.seat;

import java.util.List;

public record BulkSeatIdsRequest (
    List<Long> seatIds
){}
