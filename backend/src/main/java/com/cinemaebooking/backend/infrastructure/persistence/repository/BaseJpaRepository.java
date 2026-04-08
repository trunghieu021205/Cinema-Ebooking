package com.cinemaebooking.backend.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * BaseJpaRepository: Repository cơ sở cho tất cả các entity JPA trong dự án.
 * <p>
 * Hỗ trợ:
 * <ul>
 *     <li>Soft Delete (deletedAt, deletedBy)</li>
 *     <li>Hard Delete</li>
 *     <li>Find bao gồm entity đã xóa</li>
 *     <li>Phương thức tiện ích findByIdOrThrow, softDelete, restoreById</li>
 * </ul>
 * <p>
 * Chú ý: Tất cả entity phải kế thừa từ BaseJpaEntity.
 *
 * @param <T>  Entity type kế thừa BaseJpaEntity
 * @param <ID> ID type của entity
 * @author Hieu Nguyen
 * @since 2026
 */
@NoRepositoryBean
public interface BaseJpaRepository<T extends BaseJpaEntity, ID>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * Lấy entity theo ID (chỉ chưa xóa).
     * @param id ID entity
     * @return Optional entity
     */
    @Override
    Optional<T> findById(ID id);

    /**
     * Lấy tất cả entity chưa bị xóa.
     * @return List entity
     */
    @Override
    List<T> findAll();

    /**
     * Lấy entity theo ID, ném RuntimeException nếu không tìm thấy.
     * @param id ID entity
     * @return Entity
     * @throws RuntimeException nếu entity không tồn tại
     */
    default T findByIdOrThrow(ID id) {
        return findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found: " + id));
    }

    /**
     * Xóa entity theo ID (soft delete, trigger @SQLDelete).
     * @param id ID entity
     */
    @Override
    void deleteById(ID id);

    /**
     * Xóa entity (soft delete, trigger @SQLDelete).
     * @param entity entity cần xóa
     */
    @Override
    void delete(T entity);

    /**
     * Soft delete entity với thông tin người thực hiện, bypass @SQLDelete.
     * @param entity entity cần xóa mềm
     * @param deletedBy username hoặc userId thực hiện xóa
     */
    @Transactional
    default void softDelete(T entity, String deletedBy) {
        entity.markAsDeleted(deletedBy);
        save(entity); // Manual update
    }

    /**
     * Soft delete entity theo ID với deletedBy.
     * @param id ID entity
     * @param deletedBy username hoặc userId thực hiện xóa
     */
    @Transactional
    default void softDeleteById(ID id, String deletedBy) {
        T entity = findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found: " + id));
        softDelete(entity, deletedBy);
    }

    /**
     * Lấy entity theo ID bao gồm cả entity đã xóa (bỏ qua SQLRestriction).
     * @param id ID entity
     * @return Optional entity
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id")
    Optional<T> findByIdIncludingDeleted(@Param("id") ID id);

    /**
     * Lấy tất cả entity bao gồm entity đã xóa (bỏ qua SQLRestriction).
     * @return List entity
     */
    @Query("SELECT e FROM #{#entityName} e")
    List<T> findAllIncludingDeleted();

    /**
     * Hard delete (xóa vật lý) theo ID.
     * @param id ID entity
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM #{#entityName} e WHERE e.id = :id")
    void hardDeleteById(@Param("id") ID id);

    /**
     * Lấy tất cả entity đã bị soft delete (Thùng rác).
     * @return List entity đã xóa
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NOT NULL")
    List<T> findAllDeleted();

    /**
     * Khôi phục entity đã soft delete theo ID.
     * @param id ID entity
     */
    @Modifying
    @Transactional
    @Query("""
        UPDATE #{#entityName} e 
        SET e.deletedAt = NULL, 
            e.deletedBy = NULL, 
            e.updatedAt = CURRENT_TIMESTAMP 
        WHERE e.id = :id
        """)
    void restoreById(@Param("id") ID id);

    /**
     * Kiểm tra tồn tại entity theo ID (chưa xóa).
     * @param id ID entity
     * @return true nếu tồn tại, false nếu không
     */
    @Override
    boolean existsById(ID id);
}