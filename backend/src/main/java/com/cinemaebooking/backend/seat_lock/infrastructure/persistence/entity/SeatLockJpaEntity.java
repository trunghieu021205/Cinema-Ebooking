package com.cinemaebooking.backend.seat_lock.infrastructure.persistence.entity;

import com.cinemaebooking.backend.booking.infrastructure.persistence.entity.BookingJpaEntity;
import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.showtime_seat.infrastructure.persistence.entity.ShowtimeSeatJpaEntity;
import com.cinemaebooking.backend.user.infrastructure.persistence.entity.UserJpaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * SeatLockJpaEntity - Persistence model for seat_locks table.
 * Responsibility:
 * - Map database table seat_locks
 * - Handle persistence concerns only
 * - No business logic allowed
 * Note:
 * - This is NOT a domain model
 * - Must be converted via Mapper
 * - Seat locks are temporary data and must be hard deleted when expired
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(
        name = "seat_locks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_seat_locks_showtime_seat_id",
                        columnNames = {"showtime_seat_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class SeatLockJpaEntity extends BaseJpaEntity {

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_seat_id", nullable = false)
    private ShowtimeSeatJpaEntity showtimeSeat;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id",nullable = false)
    private BookingJpaEntity booking;

    @NotNull
    @Column(name = "locked_at", nullable = false)
    private LocalDateTime lockedAt;

    @NotNull
    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;
}
