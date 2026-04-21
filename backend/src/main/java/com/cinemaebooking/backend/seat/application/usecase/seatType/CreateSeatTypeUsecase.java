package com.cinemaebooking.backend.seat.application.usecase.seatType;

import com.cinemaebooking.backend.seat.application.dto.seatType.CreateSeatTypeRequest;
import com.cinemaebooking.backend.seat.application.dto.seatType.SeatTypeResponse;
import com.cinemaebooking.backend.seat.application.mapper.seatType.SeatTypeResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.model.seatType.SeatType;
import com.cinemaebooking.backend.seat.domain.validator.SeatTypeCommandValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * CreateSeatTypeUseCase - Handles creation of a new SeatType.
 */
@Service
@RequiredArgsConstructor
public class CreateSeatTypeUsecase {

    private final SeatTypeRepository seatTypeRepository;
    private final SeatTypeResponseMapper mapper;
    private final SeatTypeCommandValidator validator;

    public SeatTypeResponse execute(CreateSeatTypeRequest request) {

        // ================== VALIDATION ==================
        validator.validateCreateRequest(request);

        // ================== BUILD DOMAIN ==================
        SeatType seatType = buildSeatType(request);

        // ================== PERSIST ==================
        SeatType saved = seatTypeRepository.create(seatType);

        // ================== RESPONSE ==================
        return mapper.toResponse(saved);
    }

    // ================== PRIVATE METHODS ==================

    private SeatType buildSeatType(CreateSeatTypeRequest request) {
        return SeatType.builder()
                .name(request.getName())
                .basePrice(request.getBasePrice())
                .build();
    }
}
