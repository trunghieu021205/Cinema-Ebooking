package com.cinemaebooking.backend.payment.infrastructure.mapper;

import com.cinemaebooking.backend.booking.infrastructure.persistence.entity.BookingJpaEntity;
import com.cinemaebooking.backend.booking.infrastructure.persistence.repository.BookingJpaRepository;
import com.cinemaebooking.backend.payment.domain.model.Payment;
import com.cinemaebooking.backend.payment.domain.valueObject.PaymentId;
import com.cinemaebooking.backend.payment.infrastructure.persistence.entity.PaymentJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapperImpl implements PaymentMapper {

    private final BookingJpaRepository bookingJpaRepository;

    @Override
    public Payment toDomain(PaymentJpaEntity e) {
        if (e == null) return null;

        return Payment.builder()
                .id(PaymentId.ofNullable(e.getId()))
                .paymentCode(e.getPaymentCode())
                .bookingId(e.getBooking() != null ? e.getBooking().getId() : null)
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
    public PaymentJpaEntity toEntity(Payment domain) {

        if (domain.getBookingId() == null) {
            throw new IllegalStateException("bookingId cannot be null");
        }

        BookingJpaEntity booking =
                bookingJpaRepository.getReferenceById(domain.getBookingId());

        // IMPORTANT: đảm bảo không bị detached entity leak
        if (booking.getId() == null) {
            throw new IllegalStateException("Invalid booking reference");
        }

        return PaymentJpaEntity.builder()
                .booking(booking)
                .amount(domain.getAmount())
                .method(domain.getMethod())
                .status(domain.getStatus())
                .paymentCode(domain.getPaymentCode())
                .expiredAt(domain.getExpiredAt())
                .transactionId(domain.getTransactionId())
                .providerResponse(domain.getProviderResponse())
                .paidAt(domain.getPaidAt())
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
