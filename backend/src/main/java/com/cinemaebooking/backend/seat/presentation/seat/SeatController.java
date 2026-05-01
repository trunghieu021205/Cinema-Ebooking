package com.cinemaebooking.backend.seat.presentation.seat;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.application.dto.seat.CreateSeatRequest;
import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.dto.seat.UpdateSeatRequest;
import com.cinemaebooking.backend.seat.application.usecase.seat.*;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatController {

    private final CreateSeatUsecase createSeatUsecase;
    private final UpdateSeatUsecase updateSeatUsecase;
    private final DeleteSeatUsecase deleteSeatUsecase;
    private final GetSeatByIdUsecase getSeatByIdUsecase;
    private final FindSeatsByRoomIdUsecase findSeatsByRoomIdUsecase;
    private final GetAllSeatsUsecase getAllSeatsUsecase;

    // ================== CREATE ==================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeatResponse createSeat(@Valid @RequestBody CreateSeatRequest request) {
        return createSeatUsecase.execute(request);
    }

    // ================== DETAIL ==================
    @GetMapping("/{id}")
    public SeatResponse getSeatById(@PathVariable Long id) {
        return getSeatByIdUsecase.execute(toSeatId(id));
    }

    // ================== LIST ==================
    @GetMapping
    public Page<SeatResponse> getAllSeats(
            @PageableDefault(size = 8, page = 0) Pageable pageable
    ) {
        return getAllSeatsUsecase.execute(pageable);
    }

    // ================== UPDATE ==================
    @PutMapping("/{id}")
    public SeatResponse updateSeat(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSeatRequest request
    ) {
        return updateSeatUsecase.execute(toSeatId(id), request);
    }

    // ================== DELETE ==================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSeat(@PathVariable Long id) {
        deleteSeatUsecase.execute(toSeatId(id));
    }

    // ================== LIST BY ROOM ==================
    @GetMapping("/room/{roomId}")
    public List<SeatResponse> getSeatsByRoomId(
            @PathVariable Long roomId
    ) {
        return findSeatsByRoomIdUsecase.execute(roomId);
    }

    // ================== HELPER ==================
    private SeatId toSeatId(Long id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Seat id must not be null");
        }
        return new SeatId(id);
    }
}