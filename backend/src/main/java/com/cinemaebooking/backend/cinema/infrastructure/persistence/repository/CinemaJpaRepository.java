package com.cinemaebooking.backend.cinema.infrastructure.persistence.repository;

import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.entity.CinemaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CinemaJpaRepository: Repository làm việc trực tiếp với database.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Thực hiện CRUD với bảng "cinemas"</li>
 *     <li>Sử dụng Spring Data JPA</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Chỉ làm việc với JpaEntity, không dùng Domain</li>
 *     <li>Không chứa business logic</li>
 * </ul>
 * @author Hieu Nguyen
 * @since 2026
 */
@Repository
public interface CinemaJpaRepository extends JpaRepository<CinemaJpaEntity, Long> {
    boolean existsByName(String name);

    CinemaJpaEntity findByName(String name);
}
