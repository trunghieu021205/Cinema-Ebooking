package com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.loyalty.domain.enums.LoyaltyAccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * LoyaltyAccountJpaEntity: Mapping JPA cho tài khoản loyalty của người dùng.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *   <li>Mapping bảng "loyalty_accounts"</li>
 *   <li>Lưu trữ thông tin điểm thưởng, tổng chi tiêu và tier hiện tại</li>
 *   <li>Kế thừa auditing + soft delete từ BaseJpaEntity</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *   <li>Sử dụng Loose Coupling với User (chỉ lưu userId thay vì reference trực tiếp UserJpaEntity)</li>
 *   <li>userId là duy nhất (unique) vì một User chỉ có một LoyaltyAccount</li>
 *   <li>Domain model layer sẽ giao tiếp với User thông qua userId</li>
 *   <li>Không mapping quan hệ hai chiều để tránh tight coupling giữa các domain</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "loyalty_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class LoyaltyAccountJpaEntity extends BaseJpaEntity {

    /**
     * ID của User sở hữu tài khoản loyalty này (Loose Coupling)
     *
     * <p>Một User chỉ có duy nhất một LoyaltyAccount nên userId là unique.
     */
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    /**
     * Mã số thẻ loyalty (unique, có thể tự động sinh)
     */
    @Column(unique = true, length = 50)
    private String loyaltyNumber;

    /**
     * Tổng tiền chi tiêu tích lũy của khách hàng (dùng để tính tier)
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalSpending = BigDecimal.ZERO;

    /**
     * Tổng điểm đã tích lũy trong toàn bộ thời gian (không trừ khi redeem)
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal lifetimePoints = BigDecimal.ZERO;

    /**
     * Điểm hiện tại còn lại và có thể sử dụng
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal currentPoints = BigDecimal.ZERO;

    /**
     * Tier thành viên hiện tại
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_tier_id")
    private MembershipTierJpaEntity membershipTier;

    /**
     * Thời gian hoạt động loyalty gần nhất
     */
    private LocalDateTime lastActivityDate;

    /**
     * Ngày tham gia chương trình loyalty
     */
    private LocalDateTime joinedDate;

    /**
     * Trạng thái của tài khoản loyalty
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private LoyaltyAccountStatus status = LoyaltyAccountStatus.ACTIVE;
}