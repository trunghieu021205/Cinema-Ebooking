
package com.cinemaebooking.backend.loyalty.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

public class MembershipTierId extends BaseId {
    private MembershipTierId(Long value) { super(value); }
    public static MembershipTierId of(Long value) {
        if (value == null || value <= 0) throw new IllegalArgumentException("MembershipTierId must be positive");
        return new MembershipTierId(value);
    }
    public static MembershipTierId ofNullable(Long value) { return value == null ? null : new MembershipTierId(value); }
}