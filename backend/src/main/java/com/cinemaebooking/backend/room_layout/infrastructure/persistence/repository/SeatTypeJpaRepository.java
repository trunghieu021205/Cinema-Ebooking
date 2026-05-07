package com.cinemaebooking.backend.room_layout.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.SeatTypeJpaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface SeatTypeJpaRepository extends SoftDeleteJpaRepository<SeatTypeJpaEntity> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    Optional<SeatTypeJpaEntity> findByNameIgnoreCase(String name);

    @Query("SELECT st.basePrice FROM SeatTypeJpaEntity st WHERE st.id = :id")
    Optional<BigDecimal> findBasePriceById(@Param("id") Long id);
}
