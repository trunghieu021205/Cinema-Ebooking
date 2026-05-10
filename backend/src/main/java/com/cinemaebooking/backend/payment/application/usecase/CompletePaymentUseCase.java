package com.cinemaebooking.backend.payment.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.PaymentExceptions;
import com.cinemaebooking.backend.payment.application.port.PaymentRepository;
import com.cinemaebooking.backend.payment.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompletePaymentUseCase {

    private final PaymentRepository paymentRepository;

    public void execute(String paymentCode) {

        Payment payment = paymentRepository.findByPaymentCode(paymentCode);

        if (payment.checkExpired()) {
            throw PaymentExceptions.expired(paymentCode);
        }

        String transactionId = "TXN-" + System.nanoTime();
        String providerResponse = "{ \"mock\": \"payment-success\" }";

        payment.markSuccess(transactionId, providerResponse);

        paymentRepository.update(payment);
    }
}