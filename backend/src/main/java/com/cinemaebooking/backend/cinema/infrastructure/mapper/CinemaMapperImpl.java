package com.cinemaebooking.backend.cinema.infrastructure.mapper;

import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.entity.CinemaJpaEntity;
import org.springframework.stereotype.Component;

/**
 * CinemaMapperImpl: Triển khai mapping giữa Domain (Cinema) và JpaEntity (CinemaJpaEntity)
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Chuyển đổi Domain → JpaEntity để lưu DB</li>
 *     <li>Chuyển đổi JpaEntity → Domain để xử lý business</li>
 *     <li>Đảm bảo không leak JPA vào domain layer</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Domain sử dụng CinemaId (value object)</li>
 *     <li>JpaEntity sử dụng Long (id)</li>
 *     <li>Mapper chịu trách nhiệm convert giữa 2 loại id</li>
 *     <li>Không xử lý relation (rooms) ở giai đoạn hiện tại</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Component
public class CinemaMapperImpl implements CinemaMapper {

    /**
     * Convert Domain → JpaEntity
     *
     * <p>
     * Dùng khi:
     * <ul>
     *     <li>Lưu dữ liệu xuống database</li>
     *     <li>Update entity</li>
     * </ul>
     *
     * @param domain Cinema domain object
     * @return CinemaJpaEntity tương ứng
     */
    @Override
    public CinemaJpaEntity toEntity(Cinema domain) {
        if (domain == null) {
            return null;
        }

        return CinemaJpaEntity.builder()
                // Convert CinemaId → Long (id)
                .id(domain.getId() != null ? domain.getId().getValue() : null)

                // Mapping các field cơ bản
                .name(domain.getName())
                .address(domain.getAddress())
                .city(domain.getCity())
                .status(domain.getStatus())

                // NOTE: chưa xử lý rooms ở mapper này
                .build();
    }

    /**
     * Convert JpaEntity → Domain
     *
     * <p>
     * Dùng khi:
     * <ul>
     *     <li>Đọc dữ liệu từ database</li>
     *     <li>Trả về cho application layer</li>
     * </ul>
     *
     * @param entity CinemaJpaEntity từ database
     * @return Cinema domain object tương ứng
     */
    @Override
    public Cinema toDomain(CinemaJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Cinema.builder()
                // Convert Long (id) → CinemaId
                .id(entity.getId() != null ? new CinemaId(entity.getId()) : null)

                // Mapping các field cơ bản
                .name(entity.getName())
                .address(entity.getAddress())
                .city(entity.getCity())
                .status(entity.getStatus())

                // NOTE: chưa xử lý rooms ở mapper này
                .build();
    }
    @Override
    public void updateEntity(CinemaJpaEntity entity, Cinema domain) {
        if (entity == null || domain == null) return;

        entity.setName(domain.getName());
        entity.setAddress(domain.getAddress());
        entity.setCity(domain.getCity());
        entity.setStatus(domain.getStatus());
    }
}