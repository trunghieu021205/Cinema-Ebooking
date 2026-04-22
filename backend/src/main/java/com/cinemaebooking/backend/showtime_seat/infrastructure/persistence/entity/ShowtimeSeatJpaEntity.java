package com.cinemaebooking.backend.showtime_seat.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatJpaEntity;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeJpaEntity;
import com.cinemaebooking.backend.showtime_seat.domain.enums.ShowtimeSeatStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * ShowtimeSeatJpaEntity - Persistence model for showtime_seats table.
 * Responsibility:
 * - Map database table showtime_seats
 * - Handle persistence concerns only
 * - No business logic allowed
 * Note:
 * - This is NOT a domain model
 * - Must be converted via Mapper
 * - This entity should be hard deleted (no soft delete) because it is a mapping table
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(
        name = "showtime_seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_showtime_seats_showtime_id_seat_id_deleted",
                        columnNames = {"showtime_id", "seat_id", "deleted"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ShowtimeSeatJpaEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private SeatJpaEntity seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private ShowtimeJpaEntity showtime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ShowtimeSeatStatus status;
}