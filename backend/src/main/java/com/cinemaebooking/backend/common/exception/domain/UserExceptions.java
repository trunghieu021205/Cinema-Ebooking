package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.user.domain.valueObject.UserId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * UserExceptions - Domain-specific exceptions for User.
 * Responsibility:
 * - Provide semantic exception methods for user domain
 * - Delegate to CommonExceptions for consistent error handling
 * - Improve readability in use cases & validators
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserExceptions {

    // ================== NOT FOUND ==================

    public static BaseException notFound(UserId id) {
        return new BaseException(
                ErrorCode.USER_NOT_FOUND,
                "User not found: " + id
        );
    }

    public static BaseException notFoundByEmail(String email) {
        return new BaseException(
                ErrorCode.USER_NOT_FOUND,
                "User not found with email: " + email
        );
    }

    // ================== DUPLICATE ==================

    public static BaseException duplicateEmail(String email) {
        return new BaseException(
                ErrorCode.USER_ALREADY_EXISTS,
                "User already exists with email: " + email
        );
    }

    public static BaseException duplicateUsername(String username) {
        return new BaseException(
                ErrorCode.USER_ALREADY_EXISTS,
                "User already exists with username: " + username
        );
    }


    public static BaseException duplicatePhone(String phone) {
        return new BaseException(
                ErrorCode.USER_ALREADY_EXISTS,
                "User already exists with phone number: " + phone
        );
    }
    // ================== AUTH ==================

    public static BaseException invalidCredentials() {
        return new BaseException(
                ErrorCode.USER_INVALID_CREDENTIALS,
                "Invalid email or password"
        );
    }

    public static BaseException unauthorized() {
        return new BaseException(
                ErrorCode.UNAUTHORIZED,
                "Unauthorized access"
        );
    }

    // ================== BUSINESS RULE ==================

    public static BaseException inactiveUser(UserId id) {
        return CommonExceptions.invalidInput(
                "User is inactive: " + id
        );
    }

    public static BaseException invalidEmail(String email) {
        return CommonExceptions.invalidInput(
                "Invalid email: " + email
        );
    }

    public static BaseException invalidPassword() {
        return CommonExceptions.invalidInput(
                "Password must not be empty"
        );
    }

    public static BaseException invalidRole() {
        return CommonExceptions.invalidInput(
                "User role must not be null"
        );
    }

    public static BaseException invalidStatus() {
        return CommonExceptions.invalidInput(
                "User status must not be null"
        );
    }
}