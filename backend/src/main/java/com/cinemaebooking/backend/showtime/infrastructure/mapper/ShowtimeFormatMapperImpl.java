package com.cinemaebooking.backend.showtime.infrastructure.mapper;

import com.cinemaebooking.backend.showtime.domain.model.ShowtimeFormat;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeFormatId;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeFormatJpaEntity;
import org.springframework.stereotype.Component;

/**
 * ShowtimeFormatMapperImpl: Implementation mapper chuyển đổi giữa ShowtimeFormat Domain và ShowtimeFormatJpaEntity.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Convert Domain → Entity để persist</li>
 *     <li>Convert Entity → Domain để trả về domain layer</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Domain dùng ShowtimeFormatId (BaseId)</li>
 *     <li>JpaEntity dùng Long id</li>
 *     <li>Mapper sẽ wrap/unwrap id khi mapping</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Component
public class ShowtimeFormatMapperImpl implements ShowtimeFormatMapper {

    /**
     * Convert Domain → JpaEntity
     *
     * @param domain ShowtimeFormat domain entity
     * @return ShowtimeFormatJpaEntity entity để persist
     */
    @Override
    public ShowtimeFormatJpaEntity toEntity(ShowtimeFormat domain) {
        if (domain == null) {
            return null;
        }

        return ShowtimeFormatJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .name(domain.getName())
                .extraPrice(domain.getExtraPrice())
                .build();
    }

    /**
     * Convert JpaEntity → Domain
     *
     * @param entity ShowtimeFormatJpaEntity entity từ database
     * @return ShowtimeFormat domain entity
     */
    @Override
    public ShowtimeFormat toDomain(ShowtimeFormatJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return ShowtimeFormat.builder()
                .id(entity.getId() != null ? new ShowtimeFormatId(entity.getId()) : null)
                .name(entity.getName())
                .extraPrice(entity.getExtraPrice())
                .build();
    }
}
