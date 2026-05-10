package com.cinemaebooking.backend.room_layout.application.usecase.roomLayout;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomLayoutExceptions;
import com.cinemaebooking.backend.room_layout.application.dto.roomLayout.RoomLayoutDetailResponse;
import com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat.RoomLayoutSeatResponse;
import com.cinemaebooking.backend.room_layout.application.helper.SeatGridBuilder;
import com.cinemaebooking.backend.room_layout.application.mapper.roomLayout.RoomLayoutDtoMapper;
import com.cinemaebooking.backend.room_layout.application.mapper.roomLayoutSeat.RoomLayoutSeatResponseMapper;
import com.cinemaebooking.backend.room_layout.application.port.roomLayout.RoomLayoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetRoomLayoutByDateUseCase {

    private final RoomLayoutRepository roomLayoutRepository;
    private final RoomLayoutSeatResponseMapper seatMapper;
    private final RoomLayoutDtoMapper dtoMapper;

    public RoomLayoutDetailResponse execute(Long roomId, LocalDate date) {
        if (roomId == null) {
            throw CommonExceptions.invalidInput("roomId", ErrorCategory.REQUIRED, "roomId must not be null");
        }
        if (date == null) {
            throw CommonExceptions.invalidInput("date", ErrorCategory.REQUIRED, "date must not be null");
        }

        var layout = roomLayoutRepository.findCurrentByRoomIdAndDate(roomId, date)
                .orElseThrow(() -> RoomLayoutExceptions.noLayoutForDate(roomId, date));

        List<RoomLayoutSeatResponse> seatResponses = layout.getSeats().stream()
                .map(seatMapper::toResponse)
                .toList();

        List<List<RoomLayoutSeatResponse>> grid = SeatGridBuilder.build(seatResponses, layout.getTotalRows(), layout.getTotalCols());

        return dtoMapper.toDetailResponse(layout, grid);
    }

}