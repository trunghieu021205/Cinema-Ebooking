package com.cinemaebooking.backend.payment.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.payment.domain.enums.PaymentMethod;
import com.cinemaebooking.backend.payment.domain.enums.PaymentStatus;
import com.cinemaebooking.backend.payment.domain.valueObject.PaymentId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class Payment extends BaseEntity<PaymentId> {

    private String paymentCode;
    private Long bookingId;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionId;
    private String providerResponse;
    private LocalDateTime paidAt;
    private LocalDateTime expiredAt;

    // ---------------------------------------------------------------------
    // DOMAIN LOGIC
    // ---------------------------------------------------------------------

    public void markSuccess(String transactionId, String providerResponse) {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only PENDING payments can be marked success");
        }
        this.transactionId = transactionId;
        this.providerResponse = providerResponse;
        this.status = PaymentStatus.SUCCESS;
        this.paidAt = LocalDateTime.now();
    }

    public void markFailed(String providerResponse) {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only PENDING payments can be marked failed");
        }
        this.providerResponse = providerResponse;
        this.status = PaymentStatus.FAILED;
    }

    public void markCanceled() {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only PENDING payments can be canceled");
        }
        this.status = PaymentStatus.CANCELED;
    }

    public boolean checkExpired() {
        if (this.status == PaymentStatus.PENDING
                && expiredAt != null
                && LocalDateTime.now().isAfter(expiredAt)) {
            this.status = PaymentStatus.EXPIRED;
            return true;
        }
        return false;
    }
}