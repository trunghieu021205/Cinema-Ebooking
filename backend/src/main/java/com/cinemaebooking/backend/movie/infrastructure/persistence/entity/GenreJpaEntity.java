package com.cinemaebooking.backend.movie.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * GenreJpaEntity: Mapping thể loại phim.
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class GenreJpaEntity extends BaseJpaEntity {

    /**
     * Tên thể loại (Action, Comedy,...)
     */
    @Column(nullable = false, unique = true, length = 100)
    private String name;
}