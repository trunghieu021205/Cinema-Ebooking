package com.cinemaebooking.backend.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * BaseJpaEntity: Lớp cơ sở cho tất cả entity trong persistence layer (JPA).
 *
 * Mục đích:
 * <ul>
 *     <li>Mapping trực tiếp với database</li>
 *     <li>Quản lý auditing (createdAt, updatedAt)</li>
 *     <li>Hỗ trợ soft delete ở mức database</li>
 * </ul>
 *
 * Nguyên tắc:
 * <ul>
 *     <li>KHÔNG phụ thuộc vào domain layer</li>
 *     <li>KHÔNG extends BaseEntity</li>
 *     <li>Chỉ dùng cho persistence</li>
 * </ul>
 */
@MappedSuperclass
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = """
    UPDATE #{#entityName}
    SET deleted_at = CURRENT_TIMESTAMP,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = ? AND deleted_at IS NULL
""")
@SQLRestriction("deleted_at IS NULL")
public abstract class BaseJpaEntity {

    /**
     * ID chính trong database (auto increment).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    // ====================== AUDITING ======================

    /**
     * Thời điểm tạo bản ghi.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    protected LocalDateTime createdAt;

    /**
     * Thời điểm cập nhật cuối.
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    // ====================== SOFT DELETE ======================

    /**
     * Thời điểm soft delete.
     */
    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;
    /**
     * Version dùng cho cơ chế Optimistic Locking.
     *
     * Mục đích:
     * <ul>
     *     <li>Ngăn chặn việc nhiều transaction cập nhật cùng một bản ghi cùng lúc (race condition)</li>
     *     <li>Đảm bảo tính toàn vẹn dữ liệu khi có concurrent updates</li>
     * </ul>
     *
     * Cách hoạt động:
     * <ul>
     *     <li>Mỗi lần entity được update, version sẽ tự động tăng</li>
     *     <li>Khi update, Hibernate sẽ kiểm tra version hiện tại trong DB</li>
     *     <li>Nếu version không khớp → ném ra OptimisticLockException</li>
     * </ul>
     *
     * Ứng dụng trong hệ thống:
     * <ul>
     *     <li>Tránh double booking khi nhiều người cùng đặt một ghế</li>
     *     <li>Đảm bảo chỉ một request update thành công tại một thời điểm</li>
     * </ul>
     */
    @Version
    protected Long version;
}