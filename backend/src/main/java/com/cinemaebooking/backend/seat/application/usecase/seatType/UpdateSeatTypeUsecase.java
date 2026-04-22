package com.cinemaebooking.backend.seat.application.usecase.seatType;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.SeatTypeExceptions;
import com.cinemaebooking.backend.seat.application.dto.seatType.SeatTypeResponse;
import com.cinemaebooking.backend.seat.application.dto.seatType.UpdateSeatTypeRequest;
import com.cinemaebooking.backend.seat.application.mapper.seatType.SeatTypeResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.model.seatType.SeatType;
import com.cinemaebooking.backend.seat.application.validator.SeatTypeCommandValidator;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateSeatTypeUsecase {

    private final SeatTypeRepository seatTypeRepository;
    private final SeatTypeResponseMapper mapper;
    private final SeatTypeCommandValidator validator;

    public SeatTypeResponse execute(SeatTypeId id, UpdateSeatTypeRequest request) {

        // ================== VALIDATION ==================
        validator.validateUpdateRequest(id, request);

        // ================== LOAD ==================
        SeatType seatType = loadSeatType(id);

        // ================== APPLY DOMAIN ==================
        applyUpdate(seatType, request);

        // ================== PERSIST ==================
        SeatType saved = persist(seatType);

        // ================== MAP RESPONSE ==================
        return mapper.toResponse(saved);
    }

    // ================== PRIVATE METHODS ==================

    private SeatType loadSeatType(SeatTypeId id) {
        return seatTypeRepository.findById(id)
                .orElseThrow(() ->
                        SeatTypeExceptions.notFound(id)
                );
    }

    private void applyUpdate(SeatType seatType, UpdateSeatTypeRequest request) {
        seatType.update(
                request.getName(),
                request.getBasePrice()
        );
    }

    private SeatType persist(SeatType seatType) {
        try {
            return seatTypeRepository.update(seatType);
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
            throw CommonExceptions.concurrencyConflict();
        }
    }
}
