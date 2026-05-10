package com.cinemaebooking.backend.payment.infrastructure.mapper;

import com.cinemaebooking.backend.payment.domain.model.Payment;
import com.cinemaebooking.backend.payment.domain.valueObject.PaymentId;
import com.cinemaebooking.backend.payment.infrastructure.persistence.entity.PaymentJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapperImpl implements PaymentMapper{

    @Override
    public Payment toDomain(PaymentJpaEntity e) {
        if (e == null) return null;

        return Payment.builder()
                .id(PaymentId.ofNullable(e.getId()))
                .paymentCode(e.getPaymentCode())
                .bookingId(e.getBooking().getId())
                .amount(e.getAmount())
                .method(e.getMethod())
                .status(e.getStatus())
                .transactionId(e.getTransactionId())
                .providerResponse(e.getProviderResponse())
                .paidAt(e.getPaidAt())
                .expiredAt(e.getExpiredAt())
                .build();
    }

    @Override
    public PaymentJpaEntity toEntity(Payment d) {
        return PaymentJpaEntity.builder()
                .id(d.getId() != null ? d.getId().getValue() : null)
                .paymentCode(d.getPaymentCode())
                .amount(d.getAmount())
                .method(d.getMethod())
                .status(d.getStatus())
                .transactionId(d.getTransactionId())
                .providerResponse(d.getProviderResponse())
                .paidAt(d.getPaidAt())
                .expiredAt(d.getExpiredAt())
                .build();
    }

    @Override
    public void updateEntity(Payment d, PaymentJpaEntity e) {
        e.setStatus(d.getStatus());
        e.setTransactionId(d.getTransactionId());
        e.setProviderResponse(d.getProviderResponse());
        e.setPaidAt(d.getPaidAt());
    }
}
