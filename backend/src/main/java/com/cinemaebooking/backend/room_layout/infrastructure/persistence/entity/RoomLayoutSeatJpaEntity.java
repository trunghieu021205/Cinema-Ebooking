package com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import com.cinemaebooking.backend.room_layout.domain.enums.SeatStatus;
import jakarta.persistence.*;
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
@Getter
@Setter
@Table(name = "room_layout_seats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class RoomLayoutSeatJpaEntity extends BaseJpaEntity {

    @Column(nullable = false)
    private Integer rowIndex;

    @Column(nullable = false)
    private Integer colIndex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SeatStatus status;

    @Column(name = "seat_type_id", nullable = false)
    private Long seatTypeId;

    @Column(name = "room_layout_id", nullable = false)
    private Long roomLayoutId;

    @Column(name = "couple_group_id", nullable = false)
    private Long coupleGroupId;
}