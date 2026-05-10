package com.cinemaebooking.backend.room_layout.application.mapper.roomLayout;

import com.cinemaebooking.backend.room_layout.application.dto.roomLayout.RoomLayoutDetailResponse;
import com.cinemaebooking.backend.room_layout.application.dto.roomLayout.RoomLayoutSummaryResponse;
import com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat.RoomLayoutSeatResponse;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomLayoutDtoMapper {

    public RoomLayoutSummaryResponse toSummaryResponse(RoomLayout layout) {
        if (layout == null) return null;
        return RoomLayoutSummaryResponse.builder()
                .id(layout.getId().getValue())
                .layoutVersion(layout.getLayoutVersion())
                .roomType(layout.getRoomType())
                .effectiveDate(layout.getEffectiveDate())
                .totalRows(layout.getTotalRows())
                .totalCols(layout.getTotalCols())
                .createdAt(layout.getCreatedAt())
                .build();
    }

    public RoomLayoutDetailResponse toDetailResponse(RoomLayout layout , List<List<RoomLayoutSeatResponse>> seatGrid){
        if (layout == null) return null;

        return RoomLayoutDetailResponse.builder()
                .id(layout.getId().getValue())
                .layoutVersion(layout.getLayoutVersion())
                .roomType(layout.getRoomType())
                .effectiveDate(layout.getEffectiveDate())
                .isUsed(layout.isUsed())
                .totalRows(layout.getTotalRows())
                .totalCols(layout.getTotalCols())
                .rows(seatGrid)
                .build();
    }
}