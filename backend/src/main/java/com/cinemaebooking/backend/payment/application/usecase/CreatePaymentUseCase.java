package com.cinemaebooking.backend.payment.application.usecase;

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

    public CreatePaymentResponse execute(CreatePaymentRequest request) {

        String paymentCode = "PAY-" + System.currentTimeMillis();
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(15);

        Payment payment = Payment.builder()
                .paymentCode(paymentCode)
                .bookingId(request.getBookingId())
                .amount(request.getAmount())
                .method(request.getMethod())
                .status(PaymentStatus.PENDING)
                .expiredAt(expiredAt)
                .build();

        paymentRepository.save(payment);

        return CreatePaymentResponse.builder()
                .paymentCode(paymentCode)
                .expiredAt(expiredAt)
                .build();
    }
}