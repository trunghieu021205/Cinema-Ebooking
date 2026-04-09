package com.cinemaebooking.backend.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * BaseJpaRepository: Repository cơ sở cho persistence layer.
 *
 * Mục đích:
 * <ul>
 *     <li>Làm việc trực tiếp với database thông qua JPA</li>
 *     <li>Không liên quan đến domain</li>
 * </ul>
 *
 * Nguyên tắc:
 * <ul>
 *     <li>Chỉ dùng JpaEntity</li>
 *     <li>ID luôn là kiểu của DB (Long)</li>
 * </ul>
 *
 * @param <T> JpaEntity
 */
@NoRepositoryBean
public interface BaseJpaRepository<T extends BaseJpaEntity>
        extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    /**
     * Tìm entity hoặc throw exception
     */
    default T findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found: " + id));
    }
}