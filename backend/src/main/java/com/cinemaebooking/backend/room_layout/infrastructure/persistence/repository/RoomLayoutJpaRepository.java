package com.cinemaebooking.backend.room_layout.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.RoomLayoutJpaEntity;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("SELECT l FROM RoomLayoutJpaEntity l WHERE l.roomId IN :roomIds AND l.effectiveDate = " +
            "(SELECT MAX(l2.effectiveDate) FROM RoomLayoutJpaEntity l2 WHERE l2.roomId = l.roomId AND l2.effectiveDate <= :date)")
    List<RoomLayoutJpaEntity> findCurrentByRoomIdsAndDate(@Param("roomIds") List<Long> roomIds, @Param("date") LocalDate date);

    @Modifying
    @Query("UPDATE RoomLayoutJpaEntity l SET l.used = true, l.lastUsedDate = :date WHERE l.id = :layoutId")
    int markAsUsedAndSetLastUsedDate(@Param("layoutId") Long layoutId, @Param("date") LocalDate date);

}
