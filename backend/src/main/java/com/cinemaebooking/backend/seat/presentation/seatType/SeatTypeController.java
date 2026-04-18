package com.cinemaebooking.backend.seat.presentation.seatType;

import com.cinemaebooking.backend.seat.application.dto.seatType.CreateSeatTypeRequest;
import com.cinemaebooking.backend.seat.application.dto.seatType.SeatTypeResponse;
import com.cinemaebooking.backend.seat.application.dto.seatType.UpdateSeatTypeRequest;
import com.cinemaebooking.backend.seat.application.usecase.seatType.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seat-types")
@RequiredArgsConstructor
public class SeatTypeController {

    private final CreateSeatTypeUsecase createUC;
    private final UpdateSeatTypeUsecase updateUC;
    private final GetSeatTypeByIdUsecase getByIdUC;
    private final GetSeatTypeUsecase getAllUC;
    private final DeleteSeatTypeUsecase deleteUC;

    @PostMapping
    public SeatTypeResponse create(@RequestBody @Valid CreateSeatTypeRequest request) {
        return createUC.execute(request);
    }

    @PutMapping("/{id}")
    public SeatTypeResponse update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateSeatTypeRequest request) {
        return updateUC.execute(id, request);
    }

    @GetMapping("/{id}")
    public SeatTypeResponse findById(@PathVariable Long id) {
        return getByIdUC.execute(id);
    }

    @GetMapping
    public Page<SeatTypeResponse> findAll(Pageable pageable) {
        return getAllUC.execute(pageable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deleteUC.execute(id);
    }
}