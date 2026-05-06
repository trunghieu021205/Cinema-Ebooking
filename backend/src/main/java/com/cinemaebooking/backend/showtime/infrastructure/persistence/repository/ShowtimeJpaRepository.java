package com.cinemaebooking.backend.showtime.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.showtime.domain.enums.ShowtimeStatus;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeJpaRepository extends SoftDeleteJpaRepository<ShowtimeJpaEntity> {

    @Query("""
        SELECT s FROM ShowtimeJpaEntity s
        WHERE s.deleted = false
          AND (:cinemaId IS NULL OR s.room.cinema.id = :cinemaId)
          AND (:movieId IS NULL OR s.movie.id = :movieId)
          AND (:date IS NULL OR DATE(s.startTime) = :date)
    """)
    Page<ShowtimeJpaEntity> search(
            Long cinemaId,
            Long movieId,
            LocalDate date,
            Pageable pageable
    );

    @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
        FROM ShowtimeJpaEntity s
        WHERE s.deleted = false
          AND s.room.id = :roomId
          AND (:excludeId IS NULL OR s.id <> :excludeId)
          AND s.startTime < :endTime
          AND s.endTime > :startTime
    """)
    boolean existsRoomConflict(
            Long roomId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Long excludeId
    );

    boolean existsByRoomIdAndStatusIn(Long roomId, List<ShowtimeStatus> status);
}