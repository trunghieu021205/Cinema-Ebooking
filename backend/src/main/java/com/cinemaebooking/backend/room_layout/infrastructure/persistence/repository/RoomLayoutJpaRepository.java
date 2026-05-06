package com.cinemaebooking.backend.room_layout.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.RoomLayoutJpaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomLayoutJpaRepository extends SoftDeleteJpaRepository<RoomLayoutJpaEntity> {
    boolean existsByRoomId(Long roomId);
    Optional<RoomLayoutJpaEntity> findFirstByRoomIdOrderByLayoutVersionDesc(Long roomId);
    List<RoomLayoutJpaEntity> findByRoomIdOrderByLayoutVersionDesc(Long roomId);
    Optional<RoomLayoutJpaEntity> findByRoomIdAndLayoutVersion(Long roomId, Integer layoutVersion);

    // vẫn cần @Query cho điều kiện effectiveDate <= date
    @Query("SELECT l FROM RoomLayoutJpaEntity l WHERE l.roomId = :roomId AND l.effectiveDate <= :date ORDER BY l.layoutVersion DESC")
    List<RoomLayoutJpaEntity> findCurrentByRoomIdAndDate(@Param("roomId") Long roomId, @Param("date") LocalDate date);
}
