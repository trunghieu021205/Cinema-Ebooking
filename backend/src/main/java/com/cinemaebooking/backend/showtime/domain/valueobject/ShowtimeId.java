package com.cinemaebooking.backend.showtime.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

/**
 * ShowtimeId: Value Object đại diện cho ID của Showtime trong domain.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Đảm bảo type-safe cho Showtime ID</li>
 *     <li>Tránh nhầm lẫn với các ID entity khác (MovieId, RoomId,...)</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public class ShowtimeId extends BaseId {

    /**
     * Constructor tạo ShowtimeId từ Long value.
     *
     * @param value giá trị ID thực tế
     */
    public ShowtimeId(Long value) {
        super(value);
    }
}