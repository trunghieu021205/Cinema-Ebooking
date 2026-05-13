package com.cinemaebooking.backend.payment.application.usecase;

import com.cinemaebooking.backend.booking.application.port.BookingRepository;
import com.cinemaebooking.backend.booking.domain.model.Booking;
import com.cinemaebooking.backend.booking.domain.valueObject.BookingId;
import com.cinemaebooking.backend.common.exception.domain.BookingExceptions;
import com.cinemaebooking.backend.common.exception.domain.PaymentExceptions;
import com.cinemaebooking.backend.payment.application.port.PaymentRepository;
import com.cinemaebooking.backend.payment.domain.model.Payment;
import com.cinemaebooking.backend.showtime_seat.application.port.ShowtimeSeatRepository;
import com.cinemaebooking.backend.showtime_seat.domain.model.ShowtimeSeat;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompletePaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;

    @Transactional
    public void execute(String paymentCode) {

        Payment payment = paymentRepository.findByPaymentCode(paymentCode);

        if (payment.checkExpired()) {

            paymentRepository.update(payment);

            throw PaymentExceptions.expired(paymentCode);
        }

        String transactionId = "TXN-" + System.nanoTime();
        String providerResponse = "{ \"mock\": \"payment-success\" }";

        payment.markSuccess(transactionId, providerResponse);

        Booking booking = bookingRepository.findById(payment.getBookingId())
                .orElseThrow(() ->
                        BookingExceptions.notFound(BookingId.of(payment.getBookingId()))
                );

        booking.confirm();
        booking.setPaidAt(payment.getPaidAt());
        booking.getTickets().forEach(Ticket::activate);

        List<Long> seatIds = booking.getTickets().stream()
                .map(Ticket::getShowtimeSeatId)
                .toList();

        List<ShowtimeSeat> seats =
                showtimeSeatRepository.findAllByIds(seatIds);

        seats.forEach(ShowtimeSeat::book);

        paymentRepository.update(payment);

        bookingRepository.save(booking);

        // ShowtimeSeat -> BOOKED
        seats.forEach(showtimeSeatRepository::save);
    }
}