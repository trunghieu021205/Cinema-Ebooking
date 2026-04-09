package com.cinemaebooking.backend.cinema.application.port;

import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;

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

    Cinema save(Cinema cinema);

    Optional<Cinema> findById(CinemaId id);

    List<Cinema> findAll();

    void deleteById(CinemaId id);

    boolean existsById(CinemaId id);
}
