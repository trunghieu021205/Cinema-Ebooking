package com.cinemaebooking.backend.showtime.infrastructure.mappers;

import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;
import com.cinemaebooking.backend.showtime.domain.model.Showtime;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeJpaEntity;

/**
 * ShowtimeMapper: Interface mapper chuyển đổi giữa Showtime Domain và ShowtimeJpaEntity.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Định nghĩa contract chuyển đổi Domain ↔ JpaEntity</li>
 *     <li>Đảm bảo các lớp impl tuân theo chuẩn mapping thống nhất</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Không chứa logic mapping cụ thể</li>
 *     <li>Logic mapping sẽ nằm ở ShowtimeMapperImpl</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public interface ShowtimeMapper extends BaseMapper<Showtime, ShowtimeJpaEntity> {
}