package com.example.backend.common;

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
 * Base Repository cho tất cả các Repository trong dự án.
 * Hỗ trợ Soft Delete tự động nhờ @SQLRestriction và @SQLDelete trong BaseEntity.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * Tìm entity theo ID (chỉ lấy entity chưa bị xóa mềm)
     */
    @Override
    Optional<T> findById(ID id);

    /**
     * Lấy tất cả entity chưa bị xóa mềm
     */
    @Override
    List<T> findAll();

    /**
     * Tìm entity theo ID, ném exception nếu không tìm thấy hoặc đã bị xóa.
     *
     * TODO: Sau này sẽ thay RuntimeException bằng custom exception
     */
    default T findByIdOrThrow(ID id) {
        return findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));
    }

    /**
     * Soft Delete entity theo ID
     * (Sẽ trigger @SQLDelete trong BaseEntity)
     */
    @Override
    void deleteById(ID id);

    /**
     * Soft Delete entity
     * (Sẽ trigger @SQLDelete trong BaseEntity)
     */
    @Override
    void delete(T entity);

    /**
     * Soft Delete entity và ghi rõ người thực hiện xóa
     *
     * @param entity entity cần xóa mềm
     * @param deletedBy username/email của người thực hiện xóa
     */
    default void softDelete(T entity, String deletedBy) {
        entity.markAsDeleted(deletedBy);
        save(entity);
    }

    /**
     * Soft Delete theo ID và ghi rõ người thực hiện xóa
     *
     * @param id ID của entity
     * @param deletedBy username/email của người thực hiện xóa
     */
    default void softDeleteById(ID id, String deletedBy) {
        T entity = findByIdOrThrow(id);
        softDelete(entity, deletedBy);
    }

    /**
     * Hard Delete thật sự (xóa vĩnh viễn - ít sử dụng)
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM #{#entityName} e WHERE e.id = :id")
    void hardDeleteById(@Param("id") ID id);

    /**
     * Lấy danh sách tất cả entity đã bị xóa mềm (dùng cho chức năng Thùng rác)
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NOT NULL")
    List<T> findAllDeleted();

    /**
     * Khôi phục entity đã bị xóa mềm
     */
    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} e SET e.deletedAt = NULL, e.deletedBy = NULL, e.updatedAt = CURRENT_TIMESTAMP WHERE e.id = :id")
    void restoreById(@Param("id") ID id);

    /**
     * Kiểm tra entity tồn tại và chưa bị xóa mềm
     */
    @Override
    boolean existsById(ID id);
}
