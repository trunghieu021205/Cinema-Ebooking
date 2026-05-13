package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * PaymentExceptions - Exception helpers for Payment domain.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaymentExceptions {

    // ================== NOT FOUND ==================

    public static BaseException notFound(String paymentCode) {
        return new BaseException(
                ErrorCode.PAYMENT_NOT_FOUND,
                "Không tìm thấy thanh toán.: " + paymentCode
        );
    }

    // ================== DUPLICATE ==================

    public static BaseException duplicateTransactionId(String transactionId) {
        return new BaseException(
                ErrorCode.PAYMENT_ALREADY_EXISTS,
                "Thanh toán đã tồn tại cho đặt vé này: " + transactionId
        );
    }

    // ================== BUSINESS RULE ==================

    public static BaseException expired(String paymentCode) {
        return new BaseException(
                ErrorCode.PAYMENT_EXPIRED,
                "Phiên thanh toán đã hết hạn: " + paymentCode
        );
    }

    public static BaseException invalidStatus(String paymentCode, String status) {
        return new BaseException(
                ErrorCode.PAYMENT_INVALID_STATUS,
                "Trạng thái thanh toán không hợp lệ"
        );
    }

    public static BaseException methodNotSupported(String method) {
        return new BaseException(
                ErrorCode.PAYMENT_METHOD_NOT_SUPPORTED,
                "Phương thức thanh toán không được hỗ trợ: " + method
        );
    }
}
