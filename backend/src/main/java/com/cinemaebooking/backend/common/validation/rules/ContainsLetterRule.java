package com.cinemaebooking.backend.common.validation.rules;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationContext;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;

import java.util.regex.Pattern;

/**
 * ContainsLetterRule - Ensures that a string contains at least one letter.
 * Responsibility:
 * - Validate that input contains at least one Unicode letter character
 * - Prevent meaningless inputs like only numbers or special characters
 * - Skip validation if value is null (handled by other rules)
 * Notes:
 * - Uses Unicode-aware regex (\p{L}) to support international characters
 * - Designed for validating names, titles, etc.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public class ContainsLetterRule implements ValidationRule<String> {

    private static final Pattern LETTER_PATTERN = Pattern.compile(".*\\p{L}+.*");

    @Override
    public void validate(ValidationContext<String> context) {
        String value = context.trimmed();
        if (value == null) return;

        if (!LETTER_PATTERN.matcher(value).matches()) {
            throw CommonExceptions.invalidInput(
                    context.fieldName(),
                    ErrorCategory.INVALID_VALUE,
                    "phải chứa ít nhất một ký tự chữ cái"
            );
        }
    }
}