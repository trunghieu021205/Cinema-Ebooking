package com.cinemaebooking.backend.showtime.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

/**
 * ShowtimeFormatId: Value Object đại diện cho ID của ShowtimeFormat trong domain.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Đảm bảo type-safe cho ShowtimeFormat ID</li>
 *     <li>Tránh nhầm lẫn với các ID entity khác</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public class ShowtimeFormatId extends BaseId {

    /**
     * Constructor tạo ShowtimeFormatId từ Long value.
     *
     * @param value giá trị ID thực tế
     */
    public ShowtimeFormatId(Long value) {
        super(value);
    }
}
