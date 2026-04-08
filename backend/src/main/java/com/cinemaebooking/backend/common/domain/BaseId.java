package com.cinemaebooking.backend.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * BaseId: Value Object cho tất cả entity IDs trong domain layer.
 * <p>
 * Mục đích:
 * <ul>
 *     <li>Đảm bảo type-safe ID cho các entity.</li>
 *     <li>Tránh nhầm lẫn giữa các ID khác nhau (ví dụ MovieId, UserId,...).</li>
 *     <li>Hỗ trợ equals, hashCode, toString chuẩn để so sánh và logging.</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@RequiredArgsConstructor
public abstract class BaseId {

    /**
     * Giá trị ID thực tế (thường là Long).
     */
    private final Long value;

    /**
     * So sánh Value Object này với một object khác.
     *
     * @param o object cần so sánh
     * @return true nếu cùng loại và cùng value, false nếu khác
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseId)) return false;
        BaseId baseId = (BaseId) o;
        return value.equals(baseId.value);
    }

    /**
     * Hash code dựa trên value.
     *
     * @return hash code của value
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Chuyển Value Object thành String để logging hoặc display.
     *
     * @return String của value
     */
    @Override
    public String toString() {
        return value.toString();
    }
}