package com.cinemaebooking.backend.showtime_seat.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatJpaEntity;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * ShowtimeSeatJpaEntity: Bảng trung gian giữa Showtime và Seat.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *   <li>Mapping bảng "showtime_seats"</li>
 *   <li>Xác định ghế nào thuộc suất chiếu nào</li>
 * </ul>
 *
 * <p>
 * Lưu ý quan trọng:
 * <ul>
 *   <li>Không chứa trạng thái ghế (đã tách sang SeatLock)</li>
 *   <li>Không chứa giá vé (price sẽ nằm ở Ticket)</li>
 *   <li>Không liên kết trực tiếp với Booking (sẽ liên kết gián tiếp qua Ticket)</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "showtime_seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ShowtimeSeatJpaEntity extends BaseJpaEntity {

    /**
     * Ghế ngồi
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private SeatJpaEntity seat;

    /**
     * Suất chiếu
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private ShowtimeJpaEntity showtime;

}