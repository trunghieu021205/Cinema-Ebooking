package com.cinemaebooking.backend.room.infrastructure.persistence.entity;

import com.cinemaebooking.backend.cinema.infrastructure.persistence.entity.CinemaJpaEntity;
import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeFormatJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * RoomJpaEntity: Mapping JPA cho phòng chiếu (Cinema Room).
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Mapping bảng "rooms"</li>
 *     <li>Liên kết với Cinema (N-1)</li>
 *     <li>Định nghĩa các format mà phòng hỗ trợ (2D, 3D, IMAX,...)</li>
 *     <li>Kế thừa auditing + soft delete</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Không lưu totalSeats để tránh duplicate với Seat</li>
 *     <li>Room có thể hỗ trợ nhiều format</li>
 *     <li>Showtime phải chọn format thuộc supportedFormats</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class RoomJpaEntity extends BaseJpaEntity {

    /**
     * Tên phòng chiếu
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Rạp mà phòng này thuộc về
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id", nullable = false)
    private CinemaJpaEntity cinema;

    /**
     * Danh sách format mà phòng hỗ trợ
     */
    @ManyToMany
    @JoinTable(
            name = "room_formats",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "format_id")
    )
    private Set<ShowtimeFormatJpaEntity> supportedFormats;
}