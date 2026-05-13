package com.cinemaebooking.backend.payment.application.usecase;

import com.cinemaebooking.backend.booking.application.port.BookingRepository;
import com.cinemaebooking.backend.booking.domain.model.Booking;
import com.cinemaebooking.backend.booking.domain.valueObject.BookingId;
import com.cinemaebooking.backend.booking_coupon.application.usecase.ReleaseBookingCouponUseCase;
import com.cinemaebooking.backend.common.exception.domain.BookingExceptions;
import com.cinemaebooking.backend.payment.application.port.PaymentRepository;
import com.cinemaebooking.backend.payment.domain.model.Payment;
import com.cinemaebooking.backend.ticket.application.usecase.ReleaseSeatsUseCase;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CancelPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final ReleaseSeatsUseCase releaseSeatsUseCase;
    private final ReleaseBookingCouponUseCase releaseCouponUseCase;

    @Transactional
    public void execute(String paymentCode) {

        // 1. Hủy payment
        Payment payment = paymentRepository.findByPaymentCode(paymentCode);
        payment.markCanceled();

        // 2. Lấy booking
        Booking booking = bookingRepository.findById(payment.getBookingId())
                .orElseThrow(() -> BookingExceptions.notFound(
                        BookingId.of(payment.getBookingId())));

        // 3. Hủy booking + tickets (domain logic)
        booking.cancel();
        booking.getTickets().forEach(Ticket::cancel);

        // 4. Giải phóng ghế qua ReleaseSeatsUseCase (xử lý đúng cách)
        releaseSeatsUseCase.execute(booking.getShowtimeId(), booking.getTickets());

        // 5. Giải phóng coupon nếu có
        releaseCouponUseCase.execute(booking.getId().getValue());

        // 6. Persist
        bookingRepository.save(booking);
        paymentRepository.update(payment);

        // ← Bỏ hết phần showtimeSeatRepository thủ công ở đây
    }
}