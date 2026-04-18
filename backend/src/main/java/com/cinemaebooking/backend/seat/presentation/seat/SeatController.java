package com.cinemaebooking.backend.seat.presentation.seat;

import com.cinemaebooking.backend.seat.application.dto.seat.CreateSeatRequest;
import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.dto.seat.UpdateSeatRequest;
import com.cinemaebooking.backend.seat.application.usecase.seat.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * POST /api/v1/seats
     * Tạo mới Seat
     */
    @PostMapping
    public ResponseEntity<SeatResponse> create(@Valid @RequestBody CreateSeatRequest request) {
        SeatResponse response = createSeatUsecase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/v1/seats/{id}
     * Lấy Seat theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SeatResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(getSeatByIdUsecase.execute(id));
    }

    /**
     * GET /api/v1/seats
     * Lấy danh sách Seat (pagination)
     */
    @GetMapping
    public ResponseEntity<Page<SeatResponse>> getSeatList(
            @PageableDefault(size = 8, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(getAllSeatsUsecase.execute(pageable));
    }

    /**
     * PUT /api/v1/seats/{id}
     * Cập nhật Seat
     */
    @PutMapping("/{id}")
    public ResponseEntity<SeatResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSeatRequest request) {

        return ResponseEntity.ok(updateSeatUsecase.execute(id, request));
    }

    /**
     * DELETE /api/v1/seats/{id}
     * Xoá Seat
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteSeatUsecase.execute(id);
        return ResponseEntity.ok().build(); // giống RoomController
    }

    /**
     * GET /api/v1/seats/room/{roomId}
     * Lấy danh sách Seat theo Room
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<Page<SeatResponse>> findByRoomId(
            @PathVariable Long roomId,
            @PageableDefault(size = 8, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(
                findSeatsByRoomIdUsecase.execute(roomId, pageable)
        );
    }
}