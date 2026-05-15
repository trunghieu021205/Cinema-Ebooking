package com.cinemaebooking.backend.payment.application.usecase;

import com.cinemaebooking.backend.booking.application.port.BookingRepository;
import com.cinemaebooking.backend.booking.domain.model.Booking;
import com.cinemaebooking.backend.booking.domain.valueObject.BookingId;
import com.cinemaebooking.backend.booking.infrastructure.mapper.BookingMapper;
import com.cinemaebooking.backend.booking.infrastructure.persistence.entity.BookingJpaEntity;
import com.cinemaebooking.backend.booking.infrastructure.persistence.repository.BookingJpaRepository;
import com.cinemaebooking.backend.common.exception.domain.BookingExceptions;
import com.cinemaebooking.backend.common.exception.domain.PaymentExceptions;
import com.cinemaebooking.backend.loyalty.application.usecase.transactional.AddPointsAfterBookingUseCase;
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
    private final BookingJpaRepository bookingJpaRepository;
    private final BookingMapper bookingMapper;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final AddPointsAfterBookingUseCase addPointsAfterBookingUseCase;

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

        List<ShowtimeSeat> seats = showtimeSeatRepository.findAllByIds(seatIds);
        seats.forEach(ShowtimeSeat::book);

        paymentRepository.update(payment);

        BookingJpaEntity existingEntity = bookingJpaRepository.findById(payment.getBookingId())
                .filter(e -> !e.isDeleted())
                .orElseThrow(() -> BookingExceptions.notFound(BookingId.of(payment.getBookingId())));
        bookingMapper.updateEntity(booking, existingEntity);

        // Update ticket status trong JPA entity
        for (Ticket domainTicket : booking.getTickets()) {
            existingEntity.getTickets().stream()
                    .filter(jpaTicket -> jpaTicket.getId().equals(domainTicket.getId().getValue()))
                    .findFirst()
                    .ifPresent(jpaTicket -> jpaTicket.setStatus(domainTicket.getStatus()));
        }

        bookingJpaRepository.save(existingEntity);

        seats.forEach(showtimeSeatRepository::save);

        addPointsAfterBookingUseCase.execute(
                booking.getUserId(),
                booking.getTotalTicketPrice(),
                booking.getTotalComboPrice()
        );
    }
}
