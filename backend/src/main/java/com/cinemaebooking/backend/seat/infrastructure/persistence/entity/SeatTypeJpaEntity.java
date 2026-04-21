package com.cinemaebooking.backend.seat.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * SeatTypeJpaEntity - Persistence model for seat_types table.
 * Responsibility:
 * - Map database table seat_types
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
        name = "seat_types",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_seat_types_name_deleted",
                        columnNames = {"name", "deleted"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class SeatTypeJpaEntity extends BaseJpaEntity {

    @Column(nullable = false)
    private String name;

    @Positive
    @Column(nullable = false)
    private Long basePrice;

    @Override
    protected void beforeSoftDelete() {
        this.name = markDeleted(this.name);
    }
}