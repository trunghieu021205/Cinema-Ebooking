package com.cinemaebooking.backend.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
 * BaseJpaEntity - Base class for all JPA persistence entities.
 * Responsibility:
 * - ONLY database mapping and persistence concerns
 * - Auditing (created/updated time)
 * - Soft delete support
 * - Optimistic locking
 * Note: This class is Hibernate-dependent due to @SQLDelete/@SQLRestriction.
 * Consider custom SoftDeleteListener if you need database portability.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@MappedSuperclass
@Getter
@Setter(AccessLevel.PROTECTED)           // Rất quan trọng
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SQLRestriction("deleted_at IS NULL")
public abstract class BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    // ================== Auditing Fields ==================
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    // ================== Soft Delete ==================
    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    // ================== Optimistic Locking ==================
    @Version
    protected Long version;

    // ================== SOFT DELETE METHODS ==================

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void restore() {
        this.deletedAt = null;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}