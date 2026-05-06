package com.cinemaebooking.backend.showtime_seat.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.RoomLayoutSeatJpaEntity;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeJpaEntity;
import com.cinemaebooking.backend.showtime_seat.domain.enums.ShowtimeSeatStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class ShowtimeSeatJpaEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_layout_seat_id", nullable = false)
    private RoomLayoutSeatJpaEntity roomLayoutSeat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private ShowtimeJpaEntity showtime;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ShowtimeSeatStatus status;
}