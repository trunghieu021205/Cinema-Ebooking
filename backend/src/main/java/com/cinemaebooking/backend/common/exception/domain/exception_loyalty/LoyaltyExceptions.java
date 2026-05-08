package com.cinemaebooking.backend.common.exception.domain.exception_loyalty;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.loyalty.domain.valueobject.LoyaltyAccountId;

public final class LoyaltyExceptions {
    private LoyaltyExceptions() {}
    public static BaseException notFound(LoyaltyAccountId id) {
        return new BaseException(ErrorCode.LOYALTY_ACCOUNT_NOT_FOUND, "Loyalty account not found: " + id);
    }
    public static BaseException notFoundByUserId(Long userId) {
        return new BaseException(ErrorCode.LOYALTY_ACCOUNT_NOT_FOUND, "Loyalty account not found for userId: " + userId);
    }
}