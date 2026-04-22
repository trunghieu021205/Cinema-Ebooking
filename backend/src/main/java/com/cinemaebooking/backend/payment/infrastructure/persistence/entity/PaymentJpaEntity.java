package com.cinemaebooking.backend.payment.infrastructure.persistence.entity;

import com.cinemaebooking.backend.booking.infrastructure.persistence.entity.BookingJpaEntity;
import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.payment.domain.enums.PaymentMethod;
import com.cinemaebooking.backend.payment.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * PaymentJpaEntity - Persistence model for payments table.
 * Responsibility:
 * - Map database table payments
 * - Handle persistence concerns only
 * - No business logic allowed
 * Note:
 * - This is NOT a domain model
 * - Must be converted via Mapper
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(
        name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_payments_transaction_id_deleted",
                        columnNames = {"transaction_id", "deleted"}
                ),
                @UniqueConstraint(
                        name = "uk_payments_payment_code_deleted",
                        columnNames = {"payment_code", "deleted"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PaymentJpaEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private BookingJpaEntity booking;

    @Positive
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @Column(name = "provider_response", columnDefinition = "TEXT")
    private String providerResponse;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "payment_code", nullable = false, length = 100)
    private String paymentCode;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Override
    protected void beforeSoftDelete() {
        if (this.transactionId != null) {
            this.transactionId = markDeleted(this.transactionId);
        }
        this.paymentCode = markDeleted(this.paymentCode);
    }
}