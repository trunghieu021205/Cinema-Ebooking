package com.cinemaebooking.backend.common.exception.domain.exception_loyalty;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.loyalty.domain.valueobject.EarningRuleId;

public final class EarningRuleExceptions {
    private EarningRuleExceptions() {}
    public static BaseException notFound(EarningRuleId id) {
        return new BaseException(ErrorCode.LOYALTY_RULE_INVALID, "Earning rule not found: " + id);
    }
    public static BaseException invalidType(String type) {
        return new BaseException(ErrorCode.LOYALTY_RULE_INVALID, "Invalid earning rule type: " + type);
    }
    public static BaseException invalidRate() {
        return new BaseException(ErrorCode.LOYALTY_RULE_INVALID, "Multiplier must be greater than 0");
    }
    public static BaseException duplicateRule() {
        return new BaseException(ErrorCode.LOYALTY_RULE_CONFLICT, "Earning rule already exists for this tier and type");
    }
}