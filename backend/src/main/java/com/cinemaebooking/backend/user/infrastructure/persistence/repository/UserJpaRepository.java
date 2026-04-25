package com.cinemaebooking.backend.user.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.user.infrastructure.persistence.entity.UserJpaEntity;
import com.cinemaebooking.backend.user.domain.enums.UserRole;
import com.cinemaebooking.backend.user.domain.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserJpaRepository - JPA repository for User entity.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Repository
public interface UserJpaRepository extends SoftDeleteJpaRepository<UserJpaEntity> {
    // ===== BASIC =====
    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneNumber(String phone);
    // ===== LOGIN =====
    Optional<UserJpaEntity> findByEmailAndStatus(String email, UserStatus status);

    // ===== FILTERING =====
    Page<UserJpaEntity> findByRole(UserRole role, Pageable pageable);

    Page<UserJpaEntity> findByStatus(UserStatus status, Pageable pageable);

    Page<UserJpaEntity> findByRoleAndStatus(UserRole role, UserStatus status, Pageable pageable);
}