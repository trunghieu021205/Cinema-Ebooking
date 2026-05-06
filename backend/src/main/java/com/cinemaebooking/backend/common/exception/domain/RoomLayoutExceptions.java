package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayout.RoomLayoutId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoomLayoutExceptions {

    // ================== NOT FOUND ==================
    public static BaseException notFound(RoomLayoutId id) {
        return new BaseException(ErrorCode.ROOM_LAYOUT_NOT_FOUND,
                "RoomLayout not found: " + id);
    }

    public static BaseException noCurrentLayout(Long roomId, LocalDate date) {
        return new BaseException(ErrorCode.ROOM_LAYOUT_NO_CURRENT,
                "No current layout for room " + roomId + " at date " + date);
    }

    // ================== ALREADY EXISTS ==================
    public static BaseException alreadyExists(Long roomId) {
        return new BaseException(ErrorCode.ROOM_LAYOUT_ALREADY_EXISTS,
                "Layout already generated for room " + roomId);
    }

    // ================== INVALID ==================
    public static BaseException invalidVersion(Integer version) {
        return new BaseException(ErrorCode.ROOM_LAYOUT_INVALID_VERSION,
                "Invalid layout version: " + version);
    }

    public static BaseException invalidEffectiveDate(LocalDate date) {
        return new BaseException(ErrorCode.ROOM_LAYOUT_INVALID_EFFECTIVE_DATE,
                "Invalid effective date: " + date);
    }

    // ================== SEAT RELATED ==================
    public static BaseException seatNotFound(Long seatId) {
        return new BaseException(ErrorCode.ROOM_LAYOUT_SEAT_NOT_FOUND,
                "RoomLayoutSeat not found: " + seatId);
    }

    public static BaseException seatNotBelongToCurrentLayout(Long seatId) {
        return new BaseException(ErrorCode.ROOM_LAYOUT_SEAT_NOT_BELONG_TO_CURRENT,
                "Seat " + seatId + " does not belong to current layout");
    }

    public static BaseException coupleGroupInvalid(Long groupId) {
        return new BaseException(ErrorCode.ROOM_LAYOUT_SEAT_COUPLE_GROUP_INVALID,
                "Invalid couple group: " + groupId);
    }

    public static BaseException duplicatePosition(int row, int col) {
        return new BaseException(ErrorCode.ROOM_LAYOUT_SEAT_DUPLICATE_POSITION,
                "Duplicate seat position: row " + row + ", col " + col);
    }

    public static BaseException invalidPosition(int row, int col, int maxRow, int maxCol) {
        return new BaseException(ErrorCode.ROOM_LAYOUT_SEAT_INVALID_POSITION,
                String.format("Invalid position (row:%d, col:%d). Max row: %d, max col: %d", row, col, maxRow, maxCol));
    }
}