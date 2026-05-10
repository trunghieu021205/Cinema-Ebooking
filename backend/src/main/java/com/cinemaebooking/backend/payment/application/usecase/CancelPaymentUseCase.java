package com.cinemaebooking.backend.payment.application.usecase;

import com.cinemaebooking.backend.payment.application.port.PaymentRepository;
import com.cinemaebooking.backend.payment.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelPaymentUseCase {

    private final PaymentRepository paymentRepository;

    public void execute(String paymentCode) {
        Payment payment = paymentRepository.findByPaymentCode(paymentCode);
        payment.markCanceled();
        paymentRepository.update(payment);
    }
}