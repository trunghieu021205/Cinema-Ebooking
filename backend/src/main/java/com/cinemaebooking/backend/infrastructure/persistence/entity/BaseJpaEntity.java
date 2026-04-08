package com.cinemaebooking.backend.infrastructure.persistence.entity;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.domain.BaseId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * BaseJpaEntity: Mapping JPA từ domain entity.
 * <p>
 * Lớp này kế thừa từ BaseEntity (domain) và bổ sung annotation JPA/Hibernate:
 * <ul>
 *     <li>@MappedSuperclass để JPA nhận dạng superclass</li>
 *     <li>@Id và @GeneratedValue cho mapping database</li>
 *     <li>@SQLDelete và @SQLRestriction hỗ trợ soft delete trên DB</li>
 *     <li>@EntityListeners(AuditingEntityListener.class) để tự động set createdAt/updatedAt</li>
 * </ul>
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>idJpa chỉ phục vụ JPA mapping, domain vẫn dùng BaseId</li>
 *     <li>Soft delete logic vẫn nằm trong BaseEntity</li>
 * </ul>
 *
 * @param <ID> kiểu ID domain (BaseId)
 * @author Hieu Nguyen
 * @since 2026
 */
@MappedSuperclass
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = """
    UPDATE #{#entityName} 
    SET deleted_at = CURRENT_TIMESTAMP, 
        deleted_by = ?, 
        updated_at = CURRENT_TIMESTAMP 
    WHERE id = ? AND deleted_at IS NULL
""")
@SQLRestriction("deleted_at IS NULL")
public abstract class BaseJpaEntity<ID extends BaseId>
        extends BaseEntity<ID> {

    /**
     * ID dùng để JPA mapping (auto-increment).
     * Domain layer vẫn sử dụng BaseId.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJpa;

    /**
     * Callback trước khi persist entity.
     * Tự động thiết lập createdAt và updatedAt nếu chưa có.
     */
    @PrePersist
    public void prePersist() {
        if (getCreatedAt() == null) setCreatedAt(LocalDateTime.now());
        if (getUpdatedAt() == null) setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Callback trước khi update entity.
     * Tự động cập nhật updatedAt.
     */
    @PreUpdate
    public void preUpdate() {
        setUpdatedAt(LocalDateTime.now());
    }
}