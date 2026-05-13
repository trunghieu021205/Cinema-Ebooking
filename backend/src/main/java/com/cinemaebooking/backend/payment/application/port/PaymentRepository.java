package com.cinemaebooking.backend.payment.application.port;

import com.cinemaebooking.backend.payment.domain.model.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);

    Payment findByPaymentCode(String paymentCode);

    void update(Payment payment);
}