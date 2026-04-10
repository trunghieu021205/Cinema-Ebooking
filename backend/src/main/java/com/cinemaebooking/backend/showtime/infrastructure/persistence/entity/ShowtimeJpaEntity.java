package com.cinemaebooking.backend.showtime.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.MovieJpaEntity;
import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import com.cinemaebooking.backend.showtime.domain.enums.Language;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * ShowtimeJpaEntity: Mapping JPA cho suất chiếu phim.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Mapping bảng "showtimes"</li>
 *     <li>Liên kết với Movie và Room</li>
 *     <li>Liên kết với ShowtimeFormat để xác định cách trình chiếu (2D, IMAX,...)</li>
 *     <li>Lưu thông tin ngôn ngữ trình chiếu (audio/subtitle)</li>
 *     <li>Kế thừa auditing + soft delete</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Mỗi suất chiếu thuộc 1 phim và 1 phòng</li>
 *     <li>Không cần showtimeCode vì đã có ID</li>
 *     <li>originalLanguage thuộc Movie, không lưu tại đây</li>
 *     <li>Giá vé = SeatType.basePrice + ShowtimeFormat.extraPrice</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "showtimes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ShowtimeJpaEntity extends BaseJpaEntity {

    /**
     * Thời gian bắt đầu chiếu
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * Thời gian kết thúc chiếu
     */
    @Column(nullable = false)
    private LocalDateTime endTime;

    /**
     * Ngôn ngữ âm thanh (EN, VI,...)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Language audioLanguage;

    /**
     * Ngôn ngữ phụ đề (VI, EN,...)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Language subtitleLanguage;

    /**
     * Phim được chiếu
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private MovieJpaEntity movie;

    /**
     * Phòng chiếu
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomJpaEntity room;

    /**
     * Format suất chiếu (2D, 3D, IMAX,...)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "format_id", nullable = false)
    private ShowtimeFormatJpaEntity format;
}