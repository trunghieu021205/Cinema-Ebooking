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
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetRoomLayoutUseCase {

    private final RoomLayoutRepository roomLayoutRepository;
    private final RoomLayoutSeatResponseMapper seatMapper;
    private final RoomLayoutDtoMapper dtoMapper;

    public RoomLayoutDetailResponse execute(Long roomId) {
        if (roomId == null) {
            throw CommonExceptions.invalidInput("roomId", ErrorCategory.REQUIRED,"roomId must not be null");
        }

        RoomLayout currentLayout = roomLayoutRepository.findCurrentByRoomIdAndDate(roomId, LocalDate.now())
                .orElseThrow(() -> RoomLayoutExceptions.noCurrentLayout(roomId, LocalDate.now()));

        List<RoomLayoutSeatResponse> seatResponses = currentLayout.getSeats()
                .stream()
                .map(seatMapper::toResponse)
                .toList();

        List<List<RoomLayoutSeatResponse>> grid = SeatGridBuilder.build(seatResponses, currentLayout.getTotalRows(), currentLayout.getTotalCols());

        return dtoMapper.toDetailResponse(currentLayout, grid);
    }

}