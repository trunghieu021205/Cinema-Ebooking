package com.cinemaebooking.backend.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * BaseId: Value Object cho tất cả entity IDs trong domain layer.
 *
 * Mục đích:
 * <ul>
 *     <li>Đảm bảo type-safe ID cho các entity.</li>
 *     <li>Tránh nhầm lẫn giữa các ID khác nhau (MovieId, UserId,...).</li>
 *     <li>Không phụ thuộc vào database hay JPA.</li>
 * </ul>
 *
 * Lưu ý:
 * <ul>
 *     <li>Chỉ chứa value thuần (Long, UUID,...)</li>
 *     <li>Không chứa annotation JPA</li>
 * </ul>
 */
@Getter
@RequiredArgsConstructor
public abstract class BaseId {

    /**
     * Giá trị ID thực tế.
     */
    private final Long value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseId)) return false;
        BaseId baseId = (BaseId) o;
        return value.equals(baseId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}