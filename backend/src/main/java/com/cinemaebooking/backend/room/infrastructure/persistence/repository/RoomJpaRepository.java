package com.cinemaebooking.backend.room.infrastructure.persistence.repository;

import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomJpaRepository extends JpaRepository<RoomJpaEntity, Long> {
    boolean existsByName(String name);
}
