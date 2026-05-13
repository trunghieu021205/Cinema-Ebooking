package com.cinemaebooking.backend.payment.domain.valueObject;

import com.cinemaebooking.backend.common.domain.BaseId;

public final class PaymentId extends BaseId {

    private PaymentId(Long value) {
        super(value);
    }

    public static PaymentId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("PaymentId must be positive");
        }
        return new PaymentId(value);
    }

    public static PaymentId ofNullable(Long value) {
        return value == null ? null : new PaymentId(value);
    }
}
