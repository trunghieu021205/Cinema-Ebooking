package com.cinemaebooking.backend.payment.infrastructure.adapter;

import com.cinemaebooking.backend.common.exception.domain.PaymentExceptions;
import com.cinemaebooking.backend.payment.application.port.PaymentRepository;
import com.cinemaebooking.backend.payment.domain.model.Payment;
import com.cinemaebooking.backend.payment.infrastructure.mapper.PaymentMapper;
import com.cinemaebooking.backend.payment.infrastructure.persistence.entity.PaymentJpaEntity;
import com.cinemaebooking.backend.payment.infrastructure.persistence.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository jpa;
    private final PaymentMapper mapper;

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity entity;

        if (payment.getId() == null) {
            // INSERT
            entity = mapper.toEntity(payment);
        } else {
            // UPDATE: fetch managed entity để giữ nguyên version
            entity = jpa.findById(payment.getId().getValue())
                    .orElseThrow(() -> PaymentExceptions.notFound("Payment not found: " + payment.getId().getValue()));
        }

        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public Payment findByPaymentCode(String paymentCode) {
        return jpa.findByPaymentCode(paymentCode)
                .map(mapper::toDomain)
                .orElseThrow(() -> PaymentExceptions.notFound(paymentCode));
    }

    @Override
    public void update(Payment payment) {
        PaymentJpaEntity entity = jpa.findByPaymentCode(payment.getPaymentCode())
                .orElseThrow(() -> PaymentExceptions.notFound(payment.getPaymentCode()));

        mapper.updateEntity(payment, entity);
        jpa.save(entity);
    }
}
