package com.cinemaebooking.backend.cinema.application.port;

import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.entity.CinemaJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * CinemaRepository: Port interface thuộc domain layer.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Định nghĩa contract cho persistence</li>
 *     <li>Không phụ thuộc vào JPA hay DB cụ thể</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Chỉ làm việc với Domain (Cinema)</li>
 *     <li>Không dùng JpaEntity</li>
 * </ul>
 * @author Hieu Nguyen
 * @since 2026
 */
public interface CinemaRepository {

    Cinema create(Cinema cinema);

    Cinema update(Cinema cinema);

    Optional<Cinema> findById(CinemaId id);

    Page<Cinema> findAll(Pageable pageable);

    void deleteById(CinemaId id);

    boolean existsById(CinemaId id);

    boolean existsByName(String name);

    Cinema findByName(String name);
}
