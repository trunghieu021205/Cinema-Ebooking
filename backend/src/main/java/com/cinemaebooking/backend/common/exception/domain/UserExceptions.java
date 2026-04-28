package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.user.domain.valueObject.UserId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

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
        return new BaseException(ErrorCode.USER_NOT_FOUND,
                "Không tìm thấy người dùng với id: " + id);
    }

    public static BaseException notFoundByEmail(String email) {
        return new BaseException(ErrorCode.USER_NOT_FOUND,
                "Không tìm thấy người dùng với email: " + email);
    }

    // ================== AUTH ==================

    // ⚠️ Không expose field nào sai → bảo mật
    public static BaseException invalidCredentials() {
        return new BaseException(ErrorCode.USER_INVALID_CREDENTIALS);
    }

    public static BaseException unauthorized() {
        return new BaseException(ErrorCode.UNAUTHORIZED);
    }

    // ================== BUSINESS RULE ==================

    public static BaseException inactiveUser(UserId id) {
        return new BaseException(ErrorCode.USER_ACCOUNT_DISABLED,
                "Tài khoản bị vô hiệu hóa với id: " + id); // debugMessage
    }

    public static BaseException invalidEmail(String email) {
        return new BaseException(ErrorCode.USER_INVALID_EMAIL,
                List.of(new ErrorDetail("email", ErrorCategory.INVALID_FORMAT,
                        "định dạng email không hợp lệ: " + email)));
    }

    public static BaseException invalidPassword() {
        return new BaseException(ErrorCode.USER_INVALID_PASSWORD,
                List.of(new ErrorDetail("password", ErrorCategory.REQUIRED,
                        "mật khẩu không được để trống")));
    }

    public static BaseException invalidRole() {
        return new BaseException(ErrorCode.USER_INVALID_STATUS,
                List.of(new ErrorDetail("role", ErrorCategory.REQUIRED,
                        "vai trò người dùng không được để trống")));
    }

}