package com.cinemaebooking.backend.booking_combo.infrastructure.persistence.entity;

import com.cinemaebooking.backend.booking.infrastructure.persistence.entity.BookingJpaEntity;
import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * BookingComboJpaEntity - Persistence model for booking_combos table.
 * Responsibility:
 * - Map database table booking_combos
 * - Handle persistence concerns only
 * - No business logic allowed
 * Note:
 * - This is NOT a domain model
 * - Must be converted via Mapper
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(
        name = "booking_combos",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_booking_combos_booking_id_combo_id_deleted_at",
                        columnNames = {"booking_id", "combo_id", "deleted_at"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BookingComboJpaEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private BookingJpaEntity booking;

    @Column(name = "combo_id", nullable = false)
    private Long comboId;

    @Column(name = "combo_name", nullable = false, length = 150)
    private String comboName;

    @Positive
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Positive
    @Column(nullable = false)
    private Integer quantity;

    @Positive
    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Override
    protected void beforeSoftDelete() {
        this.comboName = markDeleted(this.comboName);
    }
}