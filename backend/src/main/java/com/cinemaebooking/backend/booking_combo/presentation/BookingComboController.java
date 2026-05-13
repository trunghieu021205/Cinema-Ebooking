package com.cinemaebooking.backend.booking_combo.presentation;

import com.cinemaebooking.backend.booking_combo.application.dto.BookingComboResponse;
import com.cinemaebooking.backend.booking_combo.application.usecase.GetBookingCombosByBookingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking_combos")
@RequiredArgsConstructor
public class BookingComboController {

    private final GetBookingCombosByBookingUseCase getBookingCombosByBookingUseCase;

    @GetMapping("/booking/{bookingId}")
    public List<BookingComboResponse> getByBooking(@PathVariable Long bookingId) {
        return getBookingCombosByBookingUseCase.execute(bookingId);
    }
}
