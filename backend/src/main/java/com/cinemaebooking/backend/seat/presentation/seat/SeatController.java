package com.cinemaebooking.backend.seat.presentation.seat;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.application.dto.seat.*;
import com.cinemaebooking.backend.seat.application.usecase.seat.*;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatController {

    private final CreateSeatUsecase createSeatUsecase;
    private final UpdateSeatUsecase updateSeatUsecase;
    private final DeleteSeatUsecase deleteSeatUsecase;
    private final GetSeatByIdUsecase getSeatByIdUsecase;
    private final GetAllSeatsUsecase getAllSeatsUsecase;
    private final BulkActiveSeatUseCase bulkActiveSeatUseCase;
    private final BulkInactiveSeatUseCase bulkInactiveSeatUseCase;
    private final BulkUpdateSeatTypeUseCase bulkUpdateSeatTypeUseCase;

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

    // ================== BULK UPDATE ==================
    @PatchMapping("/bulk-activate")
    public BulkUpdateResponse bulkActivateSeats(@Valid @RequestBody BulkSeatIdsRequest request) {
        List<SeatId> seatIds = toSeatIds(request.seatIds());
        return bulkActiveSeatUseCase.execute(seatIds);
    }

    @PatchMapping("/bulk-inactivate")
    public BulkUpdateResponse bulkInactivateSeats(@Valid @RequestBody BulkSeatIdsRequest request) {
        List<SeatId> seatIds = toSeatIds(request.seatIds());
        return bulkInactiveSeatUseCase.execute(seatIds);
    }

    @PatchMapping("/bulk-type")
    public BulkUpdateResponse bulkUpdateTypeSeats(@Valid @RequestBody BulkUpdateSeatTypeRequest request) {
        List<SeatId> seatIds = toSeatIds(request.seatIds());
        return bulkUpdateSeatTypeUseCase.execute(seatIds, toSeatTypeId(request.seatTypeId()));
    }
    // ================== DELETE ==================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSeat(@PathVariable Long id) {
        deleteSeatUsecase.execute(toSeatId(id));
    }

    // ================== HELPER ==================
    private SeatId toSeatId(Long id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("seatId", ErrorCategory.REQUIRED, "seatId must not be null");
        }
        return new SeatId(id);
    }

    private SeatTypeId toSeatTypeId(Long id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("seatTypeId", ErrorCategory.REQUIRED, "seatTypeId must not be null");
        }
        return new SeatTypeId(id);
    }

    private List<SeatId> toSeatIds(List<Long> seatIds) {
        if (seatIds == null) {
            return Collections.emptyList();
        }
        return seatIds.stream().map(this::toSeatId).toList();
    }
}