package com.cinemaebooking.backend.seat.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * SeatJpaEntity: Mapping JPA cho ghế ngồi trong phòng chiếu.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Mapping bảng "seats"</li>
 *     <li>Liên kết với Room (N-1)</li>
 *     <li>Liên kết với SeatType để xác định loại ghế</li>
 *     <li>Kế thừa auditing + soft delete</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>rowLabel + columnNumber dùng để render sơ đồ ghế dễ dàng</li>
 *     <li>seatNumber là optional (dùng hiển thị: A1, B2,...)</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class SeatJpaEntity extends BaseJpaEntity {

    /**
     * Hàng ghế (A, B, C,...)
     */
    @Column(nullable = false, length = 5)
    private String rowLabel;

    /**
     * Số thứ tự ghế trong hàng (1, 2, 3,...)
     */
    @Column(nullable = false)
    private Integer columnNumber;

    /**
     * Số ghế hiển thị (ví dụ: A1, B5)
     */
    @Transient
    public String getSeatNumber() {
        return rowLabel + columnNumber;
    }
    /**
     * Loại ghế (STANDARD, VIP,...)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_type_id", nullable = false)
    private SeatTypeJpaEntity seatType;

    /**
     * Phòng chiếu chứa ghế này
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomJpaEntity room;
}