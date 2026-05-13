
package com.cinemaebooking.backend.loyalty.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

public class EarningRuleId extends BaseId {
    private EarningRuleId(Long value) { super(value); }
    public static EarningRuleId of(Long value) {
        if (value == null || value <= 0) throw new IllegalArgumentException("EarningRuleId must be positive");
        return new EarningRuleId(value);
    }
    public static EarningRuleId ofNullable(Long value) { return value == null ? null : new EarningRuleId(value); }
}