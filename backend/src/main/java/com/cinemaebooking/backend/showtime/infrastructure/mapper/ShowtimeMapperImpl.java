package com.cinemaebooking.backend.showtime.infrastructure.mapper;

import com.cinemaebooking.backend.infrastructure.mapper.BaseRelationalMapper;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.MovieJpaEntity;
import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import com.cinemaebooking.backend.showtime.domain.model.Showtime;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeFormatJpaEntity;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeJpaEntity;
import org.springframework.stereotype.Component;

/**
 * ShowtimeMapperImpl: Implementation mapper chuyển đổi giữa Showtime Domain và ShowtimeJpaEntity.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Convert Domain → JpaEntity để lưu xuống database</li>
 *     <li>Convert JpaEntity → Domain để trả về domain layer</li>
 *     <li>Resolve relationship Movie/Room/Format bằng EntityManager.getReference()</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Domain sử dụng ShowtimeId (BaseId) thay vì Long</li>
 *     <li>JpaEntity sử dụng Long id</li>
 *     <li>movieId/roomId/formatId trong domain vẫn dùng Long (foreign reference)</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Component
public class ShowtimeMapperImpl extends BaseRelationalMapper implements ShowtimeMapper {

    /**
     * Convert Domain → JpaEntity
     *
     * <p>
     * Khi mapping từ Domain sang Entity:
     * <ul>
     *     <li>Unwrap ShowtimeId → Long</li>
     *     <li>Resolve relationship bằng getReference() để tránh query DB không cần thiết</li>
     * </ul>
     *
     * @param domain Showtime domain entity
     * @return ShowtimeJpaEntity entity để persist
     */
    @Override
    public ShowtimeJpaEntity toEntity(Showtime domain) {
        if (domain == null) {
            return null;
        }

        return ShowtimeJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .audioLanguage(domain.getAudioLanguage())
                .subtitleLanguage(domain.getSubtitleLanguage())

                // Resolve relationship (lazy reference - không query DB ngay)
                .movie(getReference(MovieJpaEntity.class, domain.getMovieId()))
                .room(getReference(RoomJpaEntity.class, domain.getRoomId()))
                .format(getReference(ShowtimeFormatJpaEntity.class, domain.getFormatId()))

                .build();
    }

    /**
     * Convert JpaEntity → Domain
     *
     * <p>
     * Khi mapping từ Entity sang Domain:
     * <ul>
     *     <li>Wrap Long id → ShowtimeId</li>
     *     <li>Relationship object → foreign key ID</li>
     * </ul>
     *
     * @param entity ShowtimeJpaEntity entity từ database
     * @return Showtime domain entity
     */
    @Override
    public Showtime toDomain(ShowtimeJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Showtime.builder()
                .id(entity.getId() != null ? new ShowtimeId(entity.getId()) : null)
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .audioLanguage(entity.getAudioLanguage())
                .subtitleLanguage(entity.getSubtitleLanguage())

                // Relationship object → ID
                .movieId(entity.getMovie() != null ? entity.getMovie().getId() : null)
                .roomId(entity.getRoom() != null ? entity.getRoom().getId() : null)
                .formatId(entity.getFormat() != null ? entity.getFormat().getId() : null)

                .build();
    }
}
