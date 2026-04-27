package com.cinemaebooking.backend.common.validation.builder;

import com.cinemaebooking.backend.common.validation.rules.ContainsLetterRule;
import com.cinemaebooking.backend.common.validation.rules.LengthRule;
import com.cinemaebooking.backend.common.validation.rules.NotBlankRule;
import com.cinemaebooking.backend.common.validation.rules.PatternRule;

public class StringValidationBuilder extends ValidationBuilder<String> {

    public static StringValidationBuilder create() {
        return new StringValidationBuilder();
    }

    public StringValidationBuilder notBlank() {
        custom(new NotBlankRule());
        return this;
    }

    public StringValidationBuilder length(int min, int max) {
        custom(new LengthRule(min, max));
        return this;
    }

    public StringValidationBuilder pattern(String regex, String message) {
        custom(new PatternRule(regex, message));
        return this;
    }

    public StringValidationBuilder containsLetter() {
        custom(new ContainsLetterRule());
        return this;
    }
}
