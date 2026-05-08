package com.cinemaebooking.backend.room_layout.application.mapper.roomLayout;

import com.cinemaebooking.backend.room_layout.application.dto.roomLayout.RoomLayoutSummaryResponse;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import org.springframework.stereotype.Component;

@Component
public class RoomLayoutDtoMapper {

    public RoomLayoutSummaryResponse toSummaryResponse(RoomLayout layout) {
        if (layout == null) return null;
        return RoomLayoutSummaryResponse.builder()
                .id(layout.getId().getValue())
                .layoutVersion(layout.getLayoutVersion())
                .effectiveDate(layout.getEffectiveDate())
                .totalRows(layout.getTotalRows())
                .totalCols(layout.getTotalCols())
                .createdAt(layout.getCreatedAt())
                .build();
    }
}