package com.cinemaebooking.backend.showtime.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
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

    public void update(String name, Long extraPrice) {
        validateName(name);
        validatePrice(extraPrice);

        this.name = name;
        this.extraPrice = extraPrice;
    }

    // ================== VALIDATION ==================

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw CommonExceptions.invalidInput("Format name must not be blank");
        }
    }

    private void validatePrice(Long price) {
        if (price == null || price < 0) {
            throw CommonExceptions.invalidInput("Extra price must be >= 0");
        }
    }
}