package com.cinemaebooking.backend.common.validation.rules;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationContext;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;

/**
 * LengthRule - Validation rule to check string length constraint.
 * Responsibility:
 * - Validate that input string length is within min and max bounds
 * - Ignore validation if value is null
 * - Throw exception when length is out of allowed range
 * - Serve as a reusable rule in validation engine
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public class LengthRule implements ValidationRule<String> {

    private final int min;
    private final int max;

    public LengthRule(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void validate(ValidationContext<String> context) {
        String v = context.trimmed();
        if (v == null) return;

        int len = v.length();

        if (len < min || len > max) {
            throw CommonExceptions.invalidInput(
                    context.fieldName() + " length must be between " + min + " and " + max
            );
        }
    }
}
