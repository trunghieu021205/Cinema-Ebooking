package com.cinemaebooking.backend.booking.application.usecase;

import com.cinemaebooking.backend.booking.application.port.BookingRepository;
import com.cinemaebooking.backend.booking.domain.model.Booking;
import com.cinemaebooking.backend.booking.domain.valueObject.BookingId;
import com.cinemaebooking.backend.common.exception.domain.BookingExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfirmPaymentUseCase {

    private final BookingRepository bookingRepository;

    @Transactional
    public void execute(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> BookingExceptions.notFound(BookingId.of(bookingId)));

        if (booking.isExpired()) {
            throw BookingExceptions.expired(BookingId.of(bookingId));
        }

        // Đổi trạng thái sang CONFIRMED, cập nhật paidAt
        booking.markAsPaid();

        // Sau khi markAsPaid, các module con (Ticket) sẽ được coi là "Valid" để check-in
        bookingRepository.save(booking);

        // Gửi email/thông báo tại đây (Domain Event hoặc gọi NotificationService)
    }
}
