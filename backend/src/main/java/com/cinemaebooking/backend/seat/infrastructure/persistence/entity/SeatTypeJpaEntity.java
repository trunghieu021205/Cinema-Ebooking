package com.cinemaebooking.backend.seat.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * SeatTypeJpaEntity: Mapping JPA cho loại ghế trong hệ thống.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Định nghĩa các loại ghế (STANDARD, VIP, COUPLE,...)</li>
 *     <li>Lưu giá cơ bản (base price) của từng loại ghế</li>
 *     <li>Dùng làm reference cho SeatJpaEntity</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Không nên dùng enum nếu cần thay đổi giá động</li>
 *     <li>name nên unique để tránh duplicate loại ghế</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "seat_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class SeatTypeJpaEntity extends BaseJpaEntity {

    /**
     * Tên loại ghế (STANDARD, VIP, COUPLE,...)
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Giá cơ bản của loại ghế
     */
    @Column(nullable = false)
    private Long basePrice;
}