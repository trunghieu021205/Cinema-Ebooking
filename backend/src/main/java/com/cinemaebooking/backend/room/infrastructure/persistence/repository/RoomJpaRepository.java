package com.cinemaebooking.backend.room.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomJpaRepository extends SoftDeleteJpaRepository<RoomJpaEntity> {
    boolean existsByName(String name);
    Page<RoomJpaEntity> findByCinema_Id(Long cinemaId, Pageable pageable);

    boolean existsByNameAndCinemaId(String name, Long cinemaId);

    boolean existsByNameAndCinemaIdAndIdNot(String name, Long cinemaId, Long roomId);


}
