package com.cinemaebooking.backend.showtime.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.showtime.domain.enums.Language;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Showtime: Domain entity đại diện cho một suất chiếu phim.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Đại diện thông tin suất chiếu trong domain layer</li>
 *     <li>Lưu startTime, endTime, audio/subtitle language</li>
 *     <li>Chỉ giữ reference ID tới Movie, Room, ShowtimeFormat</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Domain không phụ thuộc vào JPA Entity</li>
 *     <li>movieId/roomId/formatId là foreign identity trong domain</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class Showtime extends BaseEntity<ShowtimeId> {

    /**
     * Thời gian bắt đầu chiếu
     */
    private LocalDateTime startTime;

    /**
     * Thời gian kết thúc chiếu
     */
    private LocalDateTime endTime;

    /**
     * Ngôn ngữ âm thanh (EN, VI,...)
     */
    private Language audioLanguage;

    /**
     * Ngôn ngữ phụ đề (VI, EN,...)
     */
    private Language subtitleLanguage;

    /**
     * ID của phim được chiếu
     */
    private Long movieId;

    /**
     * ID của phòng chiếu
     */
    private Long roomId;

    /**
     * ID của format suất chiếu (2D, 3D, IMAX,...)
     */
    private Long formatId;
}