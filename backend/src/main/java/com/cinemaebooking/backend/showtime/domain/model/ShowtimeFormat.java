package com.cinemaebooking.backend.showtime.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeFormatId;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * ShowtimeFormat: Domain entity đại diện cho format trình chiếu (2D, 3D, IMAX,...).
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Lưu thông tin format trình chiếu</li>
 *     <li>Lưu giá phụ thu (extraPrice) để tính giá vé</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Đây là Domain Entity, không chứa annotation JPA</li>
 *     <li>ID dùng ShowtimeFormatId để đảm bảo type-safe</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class ShowtimeFormat extends BaseEntity<ShowtimeFormatId> {

    /**
     * Tên format (2D, 3D, IMAX,...)
     */
    private String name;

    /**
     * Giá phụ thu của format
     */
    private Long extraPrice;
}