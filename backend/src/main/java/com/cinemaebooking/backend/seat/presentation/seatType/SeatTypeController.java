package com.cinemaebooking.backend.seat.presentation.seatType;

import com.cinemaebooking.backend.seat.application.dto.seatType.CreateSeatTypeRequest;
import com.cinemaebooking.backend.seat.application.dto.seatType.SeatTypeResponse;
import com.cinemaebooking.backend.seat.application.dto.seatType.UpdateSeatTypeRequest;
import com.cinemaebooking.backend.seat.application.usecase.seatType.*;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seat_types")
@RequiredArgsConstructor
public class SeatTypeController {

    private final CreateSeatTypeUsecase createSeatTypeUsecase;
    private final UpdateSeatTypeUsecase updateSeatTypeUsecase;
    private final GetSeatTypeByIdUsecase getSeatTypeByIdUsecase;
    private final GetSeatTypeUsecase getSeatTypeUsecase;
    private final DeleteSeatTypeUsecase deleteSeatTypeUsecase;

    // ================== CREATE ==================

    @PostMapping
    public ResponseEntity<SeatTypeResponse> create(
            @Valid @RequestBody CreateSeatTypeRequest request
    ) {
        SeatTypeResponse response = createSeatTypeUsecase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ================== UPDATE ==================

    @PutMapping("/{id}")
    public ResponseEntity<SeatTypeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSeatTypeRequest request
    ) {
        SeatTypeResponse response = updateSeatTypeUsecase.execute(toSeatTypeId(id), request);
        return ResponseEntity.ok(response);
    }

    // ================== GET BY ID ==================

    @GetMapping("/{id}")
    public ResponseEntity<SeatTypeResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(getSeatTypeByIdUsecase.execute(toSeatTypeId(id)));
    }

    // ================== GET ALL ==================

    @GetMapping
    public ResponseEntity<Page<SeatTypeResponse>> getAll(
            @PageableDefault(size = 8, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(getSeatTypeUsecase.execute(pageable));
    }

    // ================== DELETE ==================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        deleteSeatTypeUsecase.execute(toSeatTypeId(id));
        return ResponseEntity.ok().build();
    }

    private SeatTypeId toSeatTypeId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("SeatType id must not be null");
        }
        return SeatTypeId.of(id);
    }
}