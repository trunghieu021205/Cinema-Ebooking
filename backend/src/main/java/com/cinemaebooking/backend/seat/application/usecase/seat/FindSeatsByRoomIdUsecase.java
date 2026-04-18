package com.cinemaebooking.backend.seat.application.usecase.seat;


import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.mapper.seat.SeatResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindSeatsByRoomIdUsecase {

    private final SeatRepository seatRepository;
    private final SeatResponseMapper mapper;

    public Page<SeatResponse> execute(Long roomId, Pageable pageable) {

        // Validate input
        if (roomId == null) {
            throw new IllegalArgumentException("RoomId must not be null");
        }

        // Gọi repository để lấy dữ liệu (có pagination)
        Page<Seat> seats = seatRepository.findByRoomId(roomId, pageable);

        // Convert Domain -> DTO Response
        return seats.map(mapper::toResponse);
    }
}
