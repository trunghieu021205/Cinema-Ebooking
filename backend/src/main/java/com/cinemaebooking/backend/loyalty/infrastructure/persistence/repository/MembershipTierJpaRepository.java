package com.cinemaebooking.backend.loyalty.infrastructure.persistence.repository;

import com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity.MembershipTierJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * MembershipTierJpaRepository: Repository làm việc trực tiếp với bảng "membership_tiers".
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Thực hiện CRUD với bảng "membership_tiers"</li>
 *     <li>Truy vấn tier theo name và tierLevel</li>
 *     <li>Hỗ trợ tìm tier phù hợp theo mức chi tiêu</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Chỉ làm việc với JpaEntity, không sử dụng Domain</li>
 *     <li>Không chứa business logic</li>
 *     <li>Logic xác định tier nên nằm ở Service layer</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Repository
public interface MembershipTierJpaRepository extends JpaRepository<MembershipTierJpaEntity, Long> {

    /**
     * Tìm tier theo tên (Basic, Silver, Gold)
     */
    Optional<MembershipTierJpaEntity> findByName(String name);

    /**
     * Kiểm tra tier có tồn tại theo tên không
     */
    boolean existsByName(String name);

    /**
     * Lấy tất cả tier theo thứ tự level tăng dần
     */
    List<MembershipTierJpaEntity> findAllByOrderByTierLevelAsc();

    /**
     * Tìm các tier có minSpendingRequired <= amount
     * (dùng để xác định tier phù hợp theo chi tiêu)
     */
    List<MembershipTierJpaEntity> findByMinSpendingRequiredLessThanEqualOrderByTierLevelDesc(BigDecimal amount);

}