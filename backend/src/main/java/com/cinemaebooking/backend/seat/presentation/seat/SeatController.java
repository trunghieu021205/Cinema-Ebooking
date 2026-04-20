package com.cinemaebooking.backend.seat.presentation.seat;

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

    // ================== CREATE ==================

    @PostMapping
    public ResponseEntity<SeatResponse> create(
            @Valid @RequestBody CreateSeatRequest request
    ) {
        SeatResponse response = createSeatUsecase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ================== GET BY ID ==================

    @GetMapping("/{id}")
    public ResponseEntity<SeatResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                getSeatByIdUsecase.execute(toSeatId(id))
        );
    }

    // ================== GET ALL ==================

    @GetMapping
    public ResponseEntity<Page<SeatResponse>> getAll(
            @PageableDefault(size = 8, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(
                getAllSeatsUsecase.execute(pageable)
        );
    }

    // ================== UPDATE ==================

    @PutMapping("/{id}")
    public ResponseEntity<SeatResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSeatRequest request
    ) {
        SeatResponse response =
                updateSeatUsecase.execute(toSeatId(id), request);

        return ResponseEntity.ok(response);
    }

    // ================== DELETE ==================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        deleteSeatUsecase.execute(toSeatId(id));
        return ResponseEntity.ok().build();
    }

    // ================== GET BY ROOM ==================

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Page<SeatResponse>> getByRoomId(
            @PathVariable Long roomId,
            @PageableDefault(size = 8, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(
                findSeatsByRoomIdUsecase.execute(roomId, pageable)
        );
    }

    // ================== HELPER ==================

    private SeatId toSeatId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Seat id must not be null");
        }
        return new SeatId(id);
    }
}