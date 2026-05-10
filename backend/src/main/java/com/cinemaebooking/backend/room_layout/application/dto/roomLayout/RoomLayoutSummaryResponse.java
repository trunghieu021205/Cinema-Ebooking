package com.cinemaebooking.backend.room_layout.application.dto.roomLayout;

import com.cinemaebooking.backend.room.domain.enums.RoomType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class RoomLayoutSummaryResponse {
    private Long id;
    private Integer layoutVersion;
    private RoomType roomType;
    private LocalDate effectiveDate;
    private Integer totalRows;
    private Integer totalCols;
    private LocalDateTime createdAt;
}
