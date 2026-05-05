package com.cinemaebooking.backend.showtime.infrastructure.mappers;

import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;
import com.cinemaebooking.backend.showtime.domain.model.ShowtimeFormat;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeFormatJpaEntity;

/**
 * ShowtimeFormatMapper: Interface mapper chuyển đổi giữa ShowtimeFormat Domain và ShowtimeFormatJpaEntity.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Định nghĩa contract mapping Domain ↔ JpaEntity</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Logic mapping cụ thể sẽ nằm ở ShowtimeFormatMapperImpl</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public interface ShowtimeFormatMapper extends BaseMapper<ShowtimeFormat, ShowtimeFormatJpaEntity> {
}