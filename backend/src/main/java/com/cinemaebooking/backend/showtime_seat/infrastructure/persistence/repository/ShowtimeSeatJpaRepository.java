package com.cinemaebooking.backend.showtime_seat.infrastructure.persistence.repository;

import com.cinemaebooking.backend.showtime_seat.domain.enums.ShowtimeSeatStatus;
import com.cinemaebooking.backend.showtime_seat.infrastructure.persistence.entity.ShowtimeSeatJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ShowtimeSeatJpaRepository: Repository làm việc trực tiếp với bảng showtime_seats.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Thực hiện CRUD với bảng "showtime_seats"</li>
 *     <li>Cung cấp các query thường dùng liên quan đến ghế theo suất chiếu</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Chỉ làm việc với JpaEntity, không sử dụng Domain</li>
 *     <li>Không chứa business logic</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Repository
public interface ShowtimeSeatJpaRepository extends JpaRepository<ShowtimeSeatJpaEntity, Long> {

    /**
     * Tìm ShowtimeSeat theo suất chiếu và ghế
     */
    Optional<ShowtimeSeatJpaEntity> findByShowtimeIdAndRoomLayoutSeatId(Long showtimeId, Long roomLayoutSeatId);

    /**
     * Lấy tất cả ghế thuộc một suất chiếu
     */
    List<ShowtimeSeatJpaEntity> findByShowtimeId(Long showtimeId);

    @Modifying
    @Query("UPDATE ShowtimeSeatJpaEntity s SET s.status = :status " +
            "WHERE s.showtime.id = :showtimeId AND s.id IN :seatIds")
    void updateStatusByShowtimeIdAndSeatIds(Long showtimeId, List<Long> seatIds, ShowtimeSeatStatus status);

}