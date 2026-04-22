package com.cinemaebooking.backend.movie.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * GenreJpaEntity - Persistence model for genres table.
 * Responsibility:
 * - Map database table genres
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
        name = "genres",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_genres_name_deleted",
                        columnNames = {"name", "deleted"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class GenreJpaEntity extends BaseJpaEntity {

    @NotNull
    @Column(nullable = false, length = 100)
    private String name;

    @Override
    protected void beforeSoftDelete() {
        this.name = markDeleted(this.name);
    }
}