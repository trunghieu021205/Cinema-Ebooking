package com.cinemaebooking.backend.loyalty.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity.LoyaltyAccountJpaEntity;
import com.cinemaebooking.backend.loyalty.domain.enums.LoyaltyAccountStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * LoyaltyAccountJpaRepository: Repository làm việc trực tiếp với bảng "loyalty_accounts".
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Thực hiện CRUD với bảng "loyalty_accounts"</li>
 *     <li>Truy vấn tài khoản loyalty theo userId và loyaltyNumber</li>
 *     <li>Hỗ trợ kiểm tra tồn tại tài khoản của user</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Chỉ làm việc với JpaEntity, không sử dụng Domain</li>
 *     <li>Không chứa business logic</li>
 *     <li>userId là unique → thường xuyên dùng để query</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Repository
public interface LoyaltyAccountJpaRepository extends SoftDeleteJpaRepository<LoyaltyAccountJpaEntity> {

    /**
     * Tìm LoyaltyAccount theo userId
     */
    Optional<LoyaltyAccountJpaEntity> findByUserId(Long userId);

    /**
     * Tìm LoyaltyAccount theo loyaltyNumber
     */
    Optional<LoyaltyAccountJpaEntity> findByLoyaltyNumber(String loyaltyNumber);

    /**
     * Kiểm tra user đã có LoyaltyAccount chưa
     */
    boolean existsByUserId(Long userId);

    /**
     * Lấy LoyaltyAccount theo trạng thái
     */
    Optional<LoyaltyAccountJpaEntity> findByUserIdAndStatus(Long userId, LoyaltyAccountStatus status);

}