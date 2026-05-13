package com.cinemaebooking.backend.payment.application.usecase;

import com.cinemaebooking.backend.booking.application.port.BookingRepository;
import com.cinemaebooking.backend.booking.domain.model.Booking;
import com.cinemaebooking.backend.booking.domain.valueObject.BookingId;
import com.cinemaebooking.backend.common.exception.domain.BookingExceptions;
import com.cinemaebooking.backend.payment.application.dto.CreatePaymentRequest;
import com.cinemaebooking.backend.payment.application.dto.CreatePaymentResponse;
import com.cinemaebooking.backend.payment.application.port.PaymentRepository;
import com.cinemaebooking.backend.payment.domain.enums.PaymentStatus;
import com.cinemaebooking.backend.payment.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreatePaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public CreatePaymentResponse execute(CreatePaymentRequest request) {

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> BookingExceptions.notFound(
                        BookingId.of(request.getBookingId())
                ));

        Payment payment = Payment.builder()
                .paymentCode("PAY-" + System.currentTimeMillis())
                .bookingId(booking.getId().getValue())
                .amount(booking.getFinalAmount())
                .method(request.getMethod())
                .status(PaymentStatus.PENDING)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();

        paymentRepository.save(payment);

        return CreatePaymentResponse.builder()
                .paymentCode(payment.getPaymentCode())
                .expiredAt(payment.getExpiredAt())
                .build();
    }
}