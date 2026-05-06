package com.cinemaebooking.backend.room.infrastructure.persistence.entity;

import com.cinemaebooking.backend.cinema.infrastructure.persistence.entity.CinemaJpaEntity;
import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.room.domain.enums.RoomStatus;
import com.cinemaebooking.backend.room.domain.enums.RoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;
/**
 * RoomJpaEntity - Persistence model for rooms table.
 * Responsibility:
 * - Map database table rooms
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
        name = "rooms",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_rooms_cinema_id_name_deleted",
                        columnNames = {"cinema_id", "name", "deleted"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class RoomJpaEntity extends BaseJpaEntity {

    @Column(nullable = false)
    private String name;

    @Positive
    @Column(nullable = false)
    private Integer numberOfRows;

    @Positive
    @Column(nullable = false)
    private Integer numberOfCols;

    @Positive
    @Column(nullable = false)
    private Integer totalSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id", nullable = false)
    private CinemaJpaEntity cinema;

    @Override
    protected void beforeSoftDelete() {
        this.name = markDeleted(this.name);
    }
}