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
 * MembershipTierJpaEntity: Mapping JPA cho phân hạng thành viên trong chương trình loyalty.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *   <li>Mapping bảng "membership_tiers"</li>
 *   <li>Lưu thông tin các cấp độ thành viên (Basic, Silver, Gold)</li>
 *   <li>Quy định điều kiện nâng hạng dựa trên tổng chi tiêu</li>
 *   <li>Kế thừa auditing + soft delete từ BaseJpaEntity</li>
 * </ul>
 *
 * <p>
 * Phân hạng thành viên theo yêu cầu:
 * <ul>
 *   <li>Basic: Tổng chi tiêu &lt; 1.500.000 VND</li>
 *   <li>Silver: 1.500.000 – 2.999.999 VND</li>
 *   <li>Gold: ≥ 3.000.000 VND</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *   <li>minSpendingRequired là ngưỡng cố định ban đầu (có thể thay đổi sau)</li>
 *   <li>Hầu hết hệ thống lớn dùng "Rolling 12 months" thay vì lifetime spending để khuyến khích chi tiêu đều</li>
 *   <li>Logic tính tier thực tế nên nằm ở LoyaltyService (dễ chuyển sang rolling sau này)</li>
 *   <li>name phải unique, tierLevel dùng để sắp xếp và so sánh</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "membership_tiers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class MembershipTierJpaEntity extends BaseJpaEntity {

    /**
     * Tên cấp độ thành viên (Basic, Silver, Gold)
     */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /**
     * Số tiền chi tiêu tối thiểu để đạt được tier này (fixed threshold)
     * - Basic: 0
     * - Silver: 1.500.000
     * - Gold: 3.000.000
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal minSpendingRequired;

    /**
     * Phần trăm giảm giá đặc biệt khi đạt tier (nếu áp dụng sau này)
     */
    @Column(precision = 5, scale = 2)
    private BigDecimal discountPercent;

    /**
     * Mô tả ngắn về lợi ích của tier
     */
    @Column(length = 500)
    private String benefitsDescription;

    /**
     * Thứ tự ưu tiên của tier (Basic = 1, Silver = 2, Gold = 3)
     */
    @Column(nullable = false)
    private Integer tierLevel;
}
