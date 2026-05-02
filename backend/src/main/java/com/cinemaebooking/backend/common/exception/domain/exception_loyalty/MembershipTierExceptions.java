package com.cinemaebooking.backend.common.exception.domain.exception_loyalty;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;

public final class MembershipTierExceptions {
    private MembershipTierExceptions() {}
    public static BaseException notFound(MembershipTierId id) {
        return CommonExceptions.resourceNotFound("Membership tier not found: " + id);
    }
    public static BaseException duplicateName(String name) {
        return CommonExceptions.resourceAlreadyExists("Membership tier name already exists: " + name);
    }
    public static BaseException inUse() {
        return CommonExceptions.resourceAlreadyExists("Membership tier is in use and cannot be deleted");
    }
    public static BaseException invalidSpending() {
        return CommonExceptions.invalidInput("Minimum spending must be >= 0");
    }
    public static BaseException invalidPointRates() {
        return CommonExceptions.invalidInput("Point rates must be > 0");
    }
}
