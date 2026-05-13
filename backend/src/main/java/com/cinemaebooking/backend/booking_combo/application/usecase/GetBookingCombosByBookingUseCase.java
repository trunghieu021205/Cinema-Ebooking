package com.cinemaebooking.backend.booking_combo.application.usecase;

import com.cinemaebooking.backend.booking_combo.application.dto.BookingComboResponse;
import com.cinemaebooking.backend.booking_combo.application.mapper.BookingComboResponseMapper;
import com.cinemaebooking.backend.booking_combo.application.port.BookingComboRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetBookingCombosByBookingUseCase {

    private final BookingComboRepository bookingComboRepository;
    private final BookingComboResponseMapper mapper;

    @Transactional(readOnly = true)
    public List<BookingComboResponse> execute(Long bookingId) {
        return bookingComboRepository.findByBookingId(bookingId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}