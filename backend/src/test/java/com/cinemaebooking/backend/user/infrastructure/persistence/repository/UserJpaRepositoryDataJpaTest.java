package com.cinemaebooking.backend.user.infrastructure.persistence.repository;

import com.cinemaebooking.backend.user.domain.enums.UserRole;
import com.cinemaebooking.backend.user.domain.enums.UserStatus;
import com.cinemaebooking.backend.user.infrastructure.persistence.entity.UserJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("UserJpaRepository Data JPA Tests")
class UserJpaRepositoryDataJpaTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserJpaEntity user1;
    private UserJpaEntity user2;

    @BeforeEach
    void setUp() {
        user1 = UserJpaEntity.builder()
                .fullName("Nguyen Van A")
                .email("a@gmail.com")
                .password("hash1")
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .dateOfBirth(LocalDate.of(1995, 1, 1))
                .build();

        user2 = UserJpaEntity.builder()
                .fullName("Tran Thi B")
                .email("b@gmail.com")
                .password("hash2")
                .role(UserRole.USER)
                .status(UserStatus.INACTIVE)
                .dateOfBirth(LocalDate.of(1996, 5, 15))
                .build();

        UserJpaEntity user3 = UserJpaEntity.builder()
                .fullName("Le Van C")
                .email("c@gmail.com")
                .password("hash3")
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .dateOfBirth(LocalDate.of(1990, 10, 20))
                .build();

        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);
        entityManager.persistAndFlush(user3);
    }

    // ===================== SINGLE RECORD QUERIES =====================

    @Nested
    @DisplayName("findByEmail")
    class FindByEmailTests {

        @Test
        @DisplayName("Should return user when email exists")
        void shouldFindByEmailWhenExists() {
            assertThat(userJpaRepository.findByEmail("a@gmail.com")).isPresent();
        }

        @Test
        @DisplayName("Should return empty when email does not exist")
        void shouldReturnEmptyWhenEmailNotFound() {
            assertThat(userJpaRepository.findByEmail("notfound@gmail.com")).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsByEmail")
    class ExistsByEmailTests {

        @Test
        @DisplayName("Should return true when email exists")
        void shouldReturnTrueWhenEmailExists() {
            assertThat(userJpaRepository.existsByEmail("b@gmail.com")).isTrue();
        }

        @Test
        @DisplayName("Should return false when email does not exist")
        void shouldReturnFalseWhenEmailNotExists() {
            assertThat(userJpaRepository.existsByEmail("unknown@gmail.com")).isFalse();
        }
    }

    // ===================== PAGINATION QUERIES =====================

    @Nested
    @DisplayName("Pagination Queries")
    class PaginationTests {

        private final Pageable pageable = PageRequest.of(0, 10);

        @Test
        @DisplayName("Should return paginated users by role")
        void shouldFindByRoleWithPagination() {
            Page<UserJpaEntity> page = userJpaRepository.findByRole(UserRole.USER, pageable);

            assertThat(page.getTotalElements()).isEqualTo(2);
            assertThat(page.getContent())
                    .extracting(UserJpaEntity::getEmail)
                    .containsExactlyInAnyOrder("a@gmail.com", "b@gmail.com");
        }

        @Test
        @DisplayName("Should return paginated users by status")
        void shouldFindByStatusWithPagination() {
            Page<UserJpaEntity> page = userJpaRepository.findByStatus(UserStatus.ACTIVE, pageable);

            assertThat(page.getTotalElements()).isEqualTo(2);
            assertThat(page.getContent())
                    .extracting(UserJpaEntity::getEmail)
                    .contains("a@gmail.com", "c@gmail.com");
        }

        @Test
        @DisplayName("Should return paginated users by role and status")
        void shouldFindByRoleAndStatusWithPagination() {
            Page<UserJpaEntity> page = userJpaRepository.findByRoleAndStatus(
                    UserRole.USER, UserStatus.ACTIVE, pageable);

            assertThat(page.getTotalElements()).isEqualTo(1);
            assertThat(page.getContent().get(0).getEmail()).isEqualTo("a@gmail.com");
        }

        @Test
        @DisplayName("Should return empty page when no match")
        void shouldReturnEmptyPageWhenNoMatch() {
            Page<UserJpaEntity> page = userJpaRepository.findByRoleAndStatus(
                    UserRole.ADMIN, UserStatus.INACTIVE, pageable);

            assertThat(page.getTotalElements()).isZero();
            assertThat(page.getContent()).isEmpty();
        }
    }

    // ===================== SOFT DELETE BEHAVIOR =====================

    @Nested
    @DisplayName("Soft Delete Behavior")
    class SoftDeleteTests {

        @Test
        @DisplayName("Should exclude soft deleted users from pagination queries")
        void shouldExcludeSoftDeletedUsersFromQueries() {
            // Soft delete user1
            userJpaRepository.delete(user1);
            entityManager.flush();

            Page<UserJpaEntity> activeCustomers = userJpaRepository.findByRoleAndStatus(
                    UserRole.USER, UserStatus.ACTIVE, PageRequest.of(0, 10));

            assertThat(activeCustomers.getTotalElements()).isEqualTo(0);
        }

        @Test
        @DisplayName("findByEmail should also respect soft delete")
        void shouldRespectSoftDeleteInFindByEmail() {
            userJpaRepository.delete(user2);
            entityManager.flush();

            assertThat(userJpaRepository.findByEmail("b@gmail.com")).isEmpty();
        }
    }
}