package com.cinemaebooking.backend.room_layout.domain.service;

import com.cinemaebooking.backend.room.domain.enums.RoomType;
import com.cinemaebooking.backend.room_layout.domain.enums.SeatStatus;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RoomLayoutCopyService {

    public RoomLayout createNextLayout(RoomLayout sourceLayout,
                                       LocalDate effectiveDate,
                                       Map<Long, SeatChange> changes,
                                       RoomType newRoomType) {
        List<RoomLayoutSeat> newSeats = sourceLayout.getSeats().stream()
                .map(oldSeat -> {
                    SeatChange change = changes.get(oldSeat.getId().getValue());
                    if (change != null) {
                        return RoomLayoutSeat.builder()
                                .id(null)
                                .roomLayoutId(null)
                                .rowIndex(oldSeat.getRowIndex())
                                .colIndex(oldSeat.getColIndex())
                                .label(oldSeat.getLabel())
                                .status(change.newStatus() != null ? change.newStatus() : oldSeat.getStatus())
                                .seatTypeId(change.newSeatTypeId() != null ? change.newSeatTypeId() : oldSeat.getSeatTypeId())
                                .coupleGroupId(oldSeat.getCoupleGroupId())
                                .build();
                    } else {
                        return RoomLayoutSeat.builder()
                                .id(null)
                                .roomLayoutId(null)
                                .rowIndex(oldSeat.getRowIndex())
                                .colIndex(oldSeat.getColIndex())
                                .label(oldSeat.getLabel())
                                .status(oldSeat.getStatus())
                                .seatTypeId(oldSeat.getSeatTypeId())
                                .coupleGroupId(oldSeat.getCoupleGroupId())
                                .build();
                    }
                })
                .collect(Collectors.toList());

        return RoomLayout.builder()
                .id(null)
                .roomId(sourceLayout.getRoomId())
                .layoutVersion(sourceLayout.getLayoutVersion() + 1)
                .effectiveDate(effectiveDate)
                .totalRows(sourceLayout.getTotalRows())
                .totalCols(sourceLayout.getTotalCols())
                .roomType(newRoomType)
                .seats(newSeats)
                .build();
    }

    public static record SeatChange(SeatStatus newStatus, Long newSeatTypeId) {}
}