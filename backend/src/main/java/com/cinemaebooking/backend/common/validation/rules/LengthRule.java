package com.cinemaebooking.backend.common.validation.rules;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationContext;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;

import java.util.Map;
import java.util.Optional;

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
    public Optional<ErrorDetail> validate(ValidationContext<String> context) {
        String v = context.trimmed();
        if (v == null) return Optional.empty();

        if (v.length() < min) {
            return Optional.of(new ErrorDetail(
                    context.fieldName(),
                    ErrorCategory.INVALID_LENGTH_MIN,
                    "tối thiểu " + min +" ký tự",
                    Map.of("min",min)
            ));
        }

        if (v.length() > max) {
            return Optional.of(new ErrorDetail(
                    context.fieldName(),
                    ErrorCategory.INVALID_LENGTH_MAX,
                    "tối đa " + min +" ký tự",
                    Map.of("max", max)
            ));
        }
        return Optional.empty();
    }
}
