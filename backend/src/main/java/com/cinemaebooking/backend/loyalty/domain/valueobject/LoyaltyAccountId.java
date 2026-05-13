
package com.cinemaebooking.backend.loyalty.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

public class LoyaltyAccountId extends BaseId {
    private LoyaltyAccountId(Long value) { super(value); }
    public static LoyaltyAccountId of(Long value) {
        if (value == null || value <= 0) throw new IllegalArgumentException("LoyaltyAccountId must be positive");
        return new LoyaltyAccountId(value);
    }
    public static LoyaltyAccountId ofNullable(Long value) { return value == null ? null : new LoyaltyAccountId(value); }
}