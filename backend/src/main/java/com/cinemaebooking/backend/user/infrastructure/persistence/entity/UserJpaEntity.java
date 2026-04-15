package com.cinemaebooking.backend.user.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.user.domain.enums.UserRole;
import com.cinemaebooking.backend.user.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * UserJpaEntity: Mapping JPA cho User domain entity.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *   <li>Mapping bảng "users"</li>
 *   <li>Kế thừa auditing + soft delete từ BaseJpaEntity</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *   <li>email phải unique</li>
 *   <li>Password được lưu dưới dạng hash</li>
 *   <li>Sử dụng Loose Coupling với LoyaltyAccount (chỉ lưu ID thay vì reference trực tiếp entity)</li>
 *   <li>Điều này giúp giảm coupling giữa các domain, dễ maintain khi hệ thống có nhiều bounded context</li>
 *   <li>Domain model layer sẽ giao tiếp với LoyaltyAccount thông qua loyaltyAccountId</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserJpaEntity extends BaseJpaEntity {

    /**
     * Tên đầy đủ của người dùng
     */
    @Column(nullable = false, length = 255)
    private String fullName;

    /**
     * Email đăng nhập (unique)
     */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /**
     * Mật khẩu đã hash
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Số điện thoại
     */
    @Column(length = 20)
    private String phoneNumber;

    /**
     * Ngày sinh của người dùng
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /**
     * Đường dẫn ảnh đại diện
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;
    
    /**
     * Vai trò người dùng (CUSTOMER, ADMIN, ...)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserRole role;

    /**
     * Trạng thái tài khoản
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserStatus status;

    /**
     * ID của Loyalty Account (Loose Coupling)
     *
     * <p>Không reference trực tiếp LoyaltyAccountJpaEntity để giảm coupling giữa User domain và Loyalty domain.
     * Domain model sẽ sử dụng loyaltyAccountId để giao tiếp.
     */
    @Column(name = "loyalty_account_id")
    private Long loyaltyAccountId;
}