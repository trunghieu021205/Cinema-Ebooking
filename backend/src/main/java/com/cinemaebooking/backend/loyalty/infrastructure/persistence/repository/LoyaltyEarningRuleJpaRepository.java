package com.cinemaebooking.backend.loyalty.infrastructure.persistence.repository;

import com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity.LoyaltyEarningRuleJpaEntity;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity.MembershipTierJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LoyaltyEarningRuleJpaRepository: Repository làm việc trực tiếp với bảng "loyalty_earning_rules".
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Thực hiện CRUD với bảng "loyalty_earning_rules"</li>
 *     <li>Truy vấn các rule tích điểm theo tier và loại giao dịch</li>
 *     <li>Hỗ trợ lấy rule đang active và sắp xếp theo priority</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Chỉ làm việc với JpaEntity, không sử dụng Domain</li>
 *     <li>Không chứa business logic</li>
 *     <li>Logic chọn rule phù hợp sẽ nằm ở Service layer</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Repository
public interface LoyaltyEarningRuleJpaRepository extends JpaRepository<LoyaltyEarningRuleJpaEntity, Long> {

    /**
     * Lấy tất cả rule theo membership tier
     */
    List<LoyaltyEarningRuleJpaEntity> findByMembershipTier(MembershipTierJpaEntity membershipTier);

    /**
     * Lấy rule theo tier và loại giao dịch
     */
    List<LoyaltyEarningRuleJpaEntity> findByMembershipTierAndEarningType(
            MembershipTierJpaEntity membershipTier,
            String earningType
    );

    /**
     * Lấy các rule đang active theo tier và loại giao dịch, sắp xếp theo priority giảm dần
     */
    List<LoyaltyEarningRuleJpaEntity> findByMembershipTierAndEarningTypeAndActiveTrueOrderByPriorityDesc(
            MembershipTierJpaEntity membershipTier,
            String earningType
    );

    /**
     * Lấy tất cả rule đang active
     */
    List<LoyaltyEarningRuleJpaEntity> findByActiveTrue();

}