package com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * LoyaltyEarningRuleJpaEntity: Mapping JPA cho quy tắc tích điểm do Admin cấu hình.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *   <li>Mapping bảng "loyalty_earning_rules"</li>
 *   <li>Quản lý cơ chế tích điểm linh hoạt theo tier và loại giao dịch</li>
 *   <li>Hỗ trợ Admin thêm/sửa/xóa quy tắc mà không cần thay đổi code</li>
 *   <li>Kế thừa auditing + soft delete từ BaseJpaEntity</li>
 * </ul>
 *
 * <p>
 * Ví dụ quy tắc hiện tại (theo yêu cầu):
 * <ul>
 *   <li>Basic  → Vé phim: 3% (0.03)</li>
 *   <li>Silver → Vé phim: 5% (0.05)</li>
 *   <li>Gold   → Vé phim: 7% (0.07)</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *   <li>Cho phép mở rộng sau này: tích điểm theo loại vé (2D/3D/IMAX), theo sản phẩm bắp nước, theo campaign...</li>
 *   <li>priority dùng để quyết định rule nào được áp dụng khi có nhiều rule trùng điều kiện</li>
 *   <li>active = false để tạm vô hiệu hóa rule mà không xóa</li>
 *   <li>Nên có index trên (membership_tier_id, earningType, active)</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(
        name = "loyalty_earning_rules",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_loyalty_earning_rules_membership_tier_id_earning_type_deleted",
                        columnNames = {"membership_tier_id", "earning_type", "deleted"}
                )
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class LoyaltyEarningRuleJpaEntity extends BaseJpaEntity {

    /**
     * Liên kết với cấp độ thành viên
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_tier_id", nullable = false)
    private MembershipTierJpaEntity membershipTier;

    /**
     * Loại giao dịch áp dụng quy tắc tích điểm
     * Ví dụ: TICKET (vé phim), CONCESSION (bắp nước), MERCHANDISE, OTHER
     */
    @Column(nullable = false, length = 100)
    private String earningType;

    /**
     * Tỷ lệ tích điểm (ví dụ: 0.03 = 3%)
     */
    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal multiplier;

    /**
     * Số điểm cố định được tặng (nếu áp dụng thay vì %)
     * Thường để null nếu dùng multiplier
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal fixedPoints;

    /**
     * Mô tả ngắn về quy tắc này
     */
    @Column(length = 500)
    private String description;

    /**
     * Điều kiện bổ sung (có thể lưu dạng JSON đơn giản hoặc mô tả text)
     * Ví dụ: {"minAmount": 100000, "dayOfWeek": "FRIDAY"}
     */
    @Column(length = 1000)
    private String conditions;

    /**
     * Quy tắc có đang hoạt động hay không
     */
    @Column(nullable = false)
    private Boolean active = true;

    /**
     * Độ ưu tiên của rule (rule có priority cao hơn sẽ được ưu tiên áp dụng)
     */
    @Column(nullable = false)
    private Integer priority = 0;
    @Override
    protected void beforeSoftDelete() {
        this.earningType = markDeleted(this.earningType);
    }
}