package com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat;

import java.util.List;

public record BulkUpdateResponse(
        int successCount,
        List<BulkError> errors
) {
    public record BulkError(Long seatId, String reason) {}
}
