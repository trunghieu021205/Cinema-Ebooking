package com.cinemaebooking.backend.seat.presentation.seatType;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/seat_types")
@RequiredArgsConstructor
public class SeatTypeController {

    private final CreateSeatTypeUsecase createSeatTypeUsecase;
    private final UpdateSeatTypeUsecase updateSeatTypeUsecase;
    private final GetSeatTypeByIdUsecase getSeatTypeByIdUsecase;
    private final GetSeatTypeUsecase getSeatTypeUsecase;
    private final DeleteSeatTypeUsecase deleteSeatTypeUsecase;

    // ================== CREATE ==================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeatTypeResponse createSeatType(@Valid @RequestBody CreateSeatTypeRequest request) {
        return createSeatTypeUsecase.execute(request);
    }

    // ================== UPDATE ==================
    @PutMapping("/{id}")
    public SeatTypeResponse updateSeatType(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSeatTypeRequest request
    ) {
        return updateSeatTypeUsecase.execute(toSeatTypeId(id), request);
    }

    // ================== DETAIL ==================
    @GetMapping("/{id}")
    public SeatTypeResponse getSeatTypeById(@PathVariable Long id) {
        return getSeatTypeByIdUsecase.execute(toSeatTypeId(id));
    }

    // ================== LIST ==================
    @GetMapping
    public Page<SeatTypeResponse> getAllSeatTypes(
            @PageableDefault(size = 8, page = 0) Pageable pageable
    ) {
        return getSeatTypeUsecase.execute(pageable);
    }

    // ================== DELETE ==================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSeatType(@PathVariable Long id) {
        deleteSeatTypeUsecase.execute(toSeatTypeId(id));
    }

    // ================== HELPER ==================
    private SeatTypeId toSeatTypeId(Long id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("SeatType id must not be null");
        }
        return SeatTypeId.of(id);
    }
}