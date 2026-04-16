package com.cinemaebooking.backend.infrastructure.mapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * BaseRelationalMapper: Lớp base hỗ trợ mapper cho các domain có quan hệ.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Hỗ trợ resolve reference từ ID → JpaEntity</li>
 *     <li>Sử dụng EntityManager để tránh query không cần thiết</li>
 *     <li>Dùng cho các mapper có relationship (ManyToOne, OneToMany,...)</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Chỉ dùng cho infrastructure layer</li>
 *     <li>Không chứa business logic</li>
 *     <li>Không thay thế BaseMapper, mà bổ sung cho mapper có quan hệ</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public abstract class BaseRelationalMapper {

    /**
     * EntityManager dùng để lấy reference entity (lazy, không query DB)
     */
    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * Lấy reference của JpaEntity từ ID mà không cần query database.
     *
     * <p>
     * Dùng trong trường hợp mapping từ Domain → JpaEntity khi Domain chỉ chứa ID.
     *
     * @param clazz Class của JpaEntity
     * @param id ID của entity
     * @param <T> Kiểu JpaEntity
     * @return JpaEntity reference (proxy)
     */
    protected <T> T getReference(Class<T> clazz, Long id) {
        if (id == null) {
            return null;
        }
        return entityManager.getReference(clazz, id);
    }
}