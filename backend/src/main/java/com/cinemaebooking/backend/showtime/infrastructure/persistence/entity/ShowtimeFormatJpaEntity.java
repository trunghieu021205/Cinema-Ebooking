package com.cinemaebooking.backend.showtime.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * ShowtimeFormatJpaEntity: Mapping JPA cho format suất chiếu.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Định nghĩa các format trình chiếu (2D, 3D, IMAX,...)</li>
 *     <li>Lưu giá phụ thu (extra price) của từng format</li>
 *     <li>Dùng để tính giá vé kết hợp với SeatType</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Format là cách trình chiếu, không phải chỉ là "type"</li>
 *     <li>Không nên dùng enum vì cần mở rộng linh hoạt</li>
 *     <li>name nên unique để tránh trùng format</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "showtime_formats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ShowtimeFormatJpaEntity extends BaseJpaEntity {

    /**
     * Tên format (2D, 3D, IMAX,...)
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Giá phụ thu của format
     */
    @Column(nullable = false)
    private Long extraPrice;
}