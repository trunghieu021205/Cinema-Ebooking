package com.cinemaebooking.backend.user.infrastructure.persistence.repository;

import com.cinemaebooking.backend.user.infrastructure.persistence.entity.UserJpaEntity;
import com.cinemaebooking.backend.user.domain.enums.UserRole;
import com.cinemaebooking.backend.user.domain.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserJpaRepository: Repository làm việc trực tiếp với bảng "users".
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Thực hiện CRUD với bảng "users"</li>
 *     <li>Truy vấn người dùng theo email, role và status</li>
 *     <li>Hỗ trợ kiểm tra tồn tại user</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Chỉ làm việc với JpaEntity, không sử dụng Domain</li>
 *     <li>Không chứa business logic</li>
 *     <li>Email là unique → thường xuyên dùng để query</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    /**
     * Tìm user theo email
     */
    Optional<UserJpaEntity> findByEmail(String email);

    /**
     * Kiểm tra email đã tồn tại chưa
     */
    boolean existsByEmail(String email);

    /**
     * Lấy danh sách user theo role
     */
    List<UserJpaEntity> findByRole(UserRole role);

    /**
     * Lấy danh sách user theo status
     */
    List<UserJpaEntity> findByStatus(UserStatus status);

    /**
     * Lấy user theo role và status
     */
    List<UserJpaEntity> findByRoleAndStatus(UserRole role, UserStatus status);

}