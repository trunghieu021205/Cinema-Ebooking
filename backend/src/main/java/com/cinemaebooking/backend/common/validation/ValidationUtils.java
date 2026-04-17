package com.cinemaebooking.backend.common.validation;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;

/**
 * ValidationUtils - Common reusable validation helpers.
 * Responsibility:
 * - Validate primitive types (String, number, etc.)
 * - Reusable across all domains
 * - No business logic
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public final class ValidationUtils {

    private ValidationUtils() {
    }

    // ================== NULL / BLANK ==================

    public static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw CommonExceptions.invalidInput(fieldName + " must not be null");
        }
    }

    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw CommonExceptions.invalidInput(fieldName + " must not be blank");
        }
    }

    // ================== LENGTH ==================

    public static void requireLength(String value, String fieldName, int min, int max) {
        if (value == null) return;

        int length = value.trim().length();

        if (length < min || length > max) {
            throw CommonExceptions.invalidInput(
                    fieldName + " length must be between " + min + " and " + max
            );
        }
    }

    // ================== STRING CONTENT ==================

    /**
     * Reject string that contains only digits (e.g. "123456").
     */
    public static void requireNotNumericOnly(String value, String fieldName) {
        if (value == null) return;

        if (value.trim().matches("^\\d+$")) {
            throw CommonExceptions.invalidInput(fieldName + " must not be numeric only");
        }
    }

    /**
     * Validate a standard string field (non-blank + length).
     */
    public static void validateString(String value, String fieldName, int min, int max) {
        requireNonBlank(value, fieldName);
        requireLength(value, fieldName, min, max);
    }


    // ================== ENUM ==================

    public static <T> void requireNonNullEnum(T value, String fieldName) {
        if (value == null) {
            throw CommonExceptions.invalidInput(fieldName + " must not be null");
        }
    }
}