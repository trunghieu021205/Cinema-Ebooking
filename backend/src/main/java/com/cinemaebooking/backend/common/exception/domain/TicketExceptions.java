package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TicketExceptions {

    // ================== NOT FOUND ==================

    public static BaseException notFound(String ticketCode) {
        return new BaseException(
                ErrorCode.TICKET_NOT_FOUND,
                "Không tìm thấy vé với mã: " + ticketCode
        );
    }

    public static BaseException notFound(Long ticketId) {
        return new BaseException(
                ErrorCode.TICKET_NOT_FOUND,
                "Không tìm thấy vé với ID: " + ticketId
        );
    }

    // ================== BUSINESS RULE ==================

    public static BaseException alreadyUsed(String ticketCode) {
        return new BaseException(
                ErrorCode.TICKET_ALREADY_USED,
                "Vé đã được sử dụng trước đó: " + ticketCode
        );
    }

    public static BaseException invalidStatus(String ticketCode, String currentStatus) {
        return new BaseException(
                ErrorCode.TICKET_INVALID_STATUS,
                String.format("Trạng thái vé %s không hợp lệ để thực hiện thao tác này (Trạng thái hiện tại: %s)",
                        ticketCode, currentStatus)
        );
    }

    // ================== TECHNICAL / SYSTEM ==================

    public static BaseException generationFailed() {
        return new BaseException(
                ErrorCode.TICKET_GENERATION_FAILED,
                "Hệ thống gặp lỗi trong quá trình tạo mã vé. Vui lòng thử lại sau."
        );
    }
}
