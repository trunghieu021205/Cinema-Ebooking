package com.cinemaebooking.backend.booking.application.usecase;

import com.cinemaebooking.backend.booking.application.port.BookingRepository;
import com.cinemaebooking.backend.booking.domain.model.Booking;
import com.cinemaebooking.backend.booking.domain.valueObject.BookingId;
import com.cinemaebooking.backend.common.exception.domain.BookingExceptions;
import com.cinemaebooking.backend.loyalty.application.usecase.transactional.AddPointsAfterBookingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfirmPaymentUseCase {

    private final BookingRepository bookingRepository;
    private final AddPointsAfterBookingUseCase addPointsAfterBookingUseCase;

    @Transactional
    public void execute(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> BookingExceptions.notFound(BookingId.of(bookingId)));

        if (booking.isExpired()) {
            throw BookingExceptions.expired(BookingId.of(bookingId));
        }

        booking.markAsPaid();

        bookingRepository.save(booking);

        addPointsAfterBookingUseCase.execute(
                booking.getUserId(),
                booking.getTotalTicketPrice(),
                booking.getTotalComboPrice()
        );
    }
}
