package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.validation.builder.StringValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import java.util.List;

public class MembershipTierValidationProfile {
    public static final MembershipTierValidationProfile INSTANCE = new MembershipTierValidationProfile();
    private MembershipTierValidationProfile() {}
    public List<ValidationRule<String>> nameRules() {
        return  StringValidationBuilder.create()
                .notBlank()
                .length(2, 100)
                .containsLetter()
                .build();
    }
}