package com.cinemaebooking.backend.cinema.infrastructure.mapper;

import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.entity.CinemaJpaEntity;
import com.cinemaebooking.backend.infrastructure.persistence.mapper.BaseMapper;


/**
 * CinemaMapper: Chuyển đổi giữa Cinema domain và CinemaJpaEntity.
 *
 * <p>
 * Trách nhiệm:
 * <ul>
 *     <li>Map Domain → JPA Entity</li>
 *     <li>Map JPA Entity → Domain</li>
 *     <li>Convert ID (CinemaId ↔ Long)</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Không xử lý business logic</li>
 *     <li>Không mapping sâu relation (Room sẽ xử lý riêng)</li>
 * </ul>
 * @author Hieu Nguyen
 * @since 2026
 */
public interface CinemaMapper extends BaseMapper<Cinema, CinemaJpaEntity> {
    void updateEntity(CinemaJpaEntity entity, Cinema domain);
}
