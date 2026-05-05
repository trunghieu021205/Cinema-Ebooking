package com.cinemaebooking.backend.seat.application.dto.seat;

import java.util.List;

public record BulkUpdateResponse(
        int successCount,
        List<BulkError> errors
) {
    public record BulkError(Long seatId, String reason) {}
}
