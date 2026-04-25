package com.cinemaebooking.backend.combo.infrastructure.persistence.repository;

import com.cinemaebooking.backend.combo.infrastructure.persistence.entity.ComboJpaEntity;
import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboJpaRepository extends SoftDeleteJpaRepository<ComboJpaEntity> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}