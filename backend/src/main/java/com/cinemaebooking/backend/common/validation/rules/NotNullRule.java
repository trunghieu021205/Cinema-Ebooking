package com.cinemaebooking.backend.common.validation.rules;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationContext;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;

/**
 * NotNullRule - Validation rule to ensure input value is not null.
 * Responsibility:
 * - Validate that input value is not null
 * - Throw exception when value is missing
 * - Serve as a basic required-field constraint in validation engine
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public class NotNullRule<T> implements ValidationRule<T> {

    @Override
    public void validate(ValidationContext<T> context) {
        if (context.value() == null) {
            throw CommonExceptions.invalidInput(
                    context.fieldName(),
                    ErrorCategory.REQUIRED,
                    "không được để trống"
            );
        }
    }
}
