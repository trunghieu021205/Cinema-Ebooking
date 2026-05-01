package com.cinemaebooking.backend.seat.application.dto.seat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSeatRequest {
    private Integer rowIndex;
    private Integer colIndex;
    private String label;      // admin tự đặt nếu tạo thủ công
    private Long seatTypeId;
    private Long roomId;
}

