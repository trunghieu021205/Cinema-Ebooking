package com.cinemaebooking.backend.seat.application.usecase.seat;


import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.mapper.seat.SeatResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FindSeatsByRoomIdUsecase {

    private final SeatRepository seatRepository;
    private final SeatResponseMapper mapper;

    public Page<SeatResponse> execute(Long roomId, Pageable pageable) {

        // Validate input
        if (roomId == null) {
            throw CommonExceptions.invalidInput("Room id must not be null");
        }

        if (pageable == null) {
            throw CommonExceptions.invalidInput("Pageable must not be null");
        }
        // Gọi repository để lấy dữ liệu (có pagination)
        Page<Seat> seats = seatRepository.findByRoomId(roomId, pageable);

        // Convert Domain -> DTO Response
        return seats.map(mapper::toResponse);
    }
}
