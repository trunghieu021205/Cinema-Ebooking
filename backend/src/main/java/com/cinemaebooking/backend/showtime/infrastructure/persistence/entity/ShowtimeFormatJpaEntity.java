package com.cinemaebooking.backend.showtime.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * ShowtimeFormatJpaEntity - Persistence model for showtime_formats table.
 * Responsibility:
 * - Map database table showtime_formats
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
        name = "showtime_formats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_showtime_formats_name_deleted",
                        columnNames = {"name", "deleted"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class ShowtimeFormatJpaEntity extends BaseJpaEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @PositiveOrZero
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal extraPrice;

    @Override
    protected void beforeSoftDelete() {
        this.name = markDeleted(this.name);
    }
}