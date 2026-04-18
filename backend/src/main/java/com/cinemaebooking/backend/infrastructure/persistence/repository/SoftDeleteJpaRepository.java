package com.cinemaebooking.backend.infrastructure.persistence.repository;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * BaseJpaRepository - Base interface for all JPA repositories.
 *
 * Responsibility:
 * - Provide common JPA operations
 * - Centralized exception handling using domain exception system
 * - No business/domain logic allowed
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@NoRepositoryBean
public interface SoftDeleteJpaRepository<T extends BaseJpaEntity>
        extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    /**
     * Find entity by id or throw standardized ResourceNotFound exception.
     */
    default T findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> CommonExceptions.resourceNotFound(
                        "Resource not found with id: " + id
                ));
    }

    void softDeleteById(Long id);
}