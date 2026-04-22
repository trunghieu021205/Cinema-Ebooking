package com.cinemaebooking.backend.seat.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * SeatJpaEntity - Persistence model for seats table.
 * Responsibility:
 * - Map database table seats
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
        name = "seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_seats_room_id_row_label_column_number_deleted",
                        columnNames = {"room_id", "row_label", "column_number", "deleted"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class SeatJpaEntity extends BaseJpaEntity {

    @Column(nullable = false, length = 50)
    private String rowLabel;

    @Positive
    @Column(nullable = false)
    private Integer columnNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_type_id", nullable = false)
    private SeatTypeJpaEntity seatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomJpaEntity room;

    @Transient
    public String getSeatNumber() {
        return rowLabel + columnNumber;
    }

    @Override
    protected void beforeSoftDelete() {
        this.rowLabel = this.markDeleted(this.rowLabel);
    }
}