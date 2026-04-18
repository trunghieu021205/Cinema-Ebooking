package com.cinemaebooking.backend.cinema.infrastructure.persistence.entity;

import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * CinemaJpaEntity - Persistence model for cinemas table.
 *
 * Responsibility:
 * - Map database table cinemas
 * - Handle persistence concerns only
 * - No business logic allowed
 *
 * Note:
 * - This is NOT a domain model
 * - Must be converted via Mapper
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "cinemas")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class CinemaJpaEntity extends BaseJpaEntity {

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, length = 150)
    private String address;

    @Column(nullable = false, length = 50)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CinemaStatus status;
}