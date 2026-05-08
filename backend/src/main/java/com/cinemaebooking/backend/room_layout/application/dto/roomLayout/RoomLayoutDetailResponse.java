package com.cinemaebooking.backend.room_layout.application.dto.roomLayout;

import com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat.RoomLayoutSeatResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class RoomLayoutDetailResponse {
    private Long id;
    private Integer layoutVersion;
    private LocalDate effectiveDate;
    int totalRows;
    int totalCols;
    List<List<RoomLayoutSeatResponse>> rows;
}
