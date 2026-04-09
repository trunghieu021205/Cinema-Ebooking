package com.cinemaebooking.backend.cinema.application.usecase;

import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * DeleteCinemaUseCase: Use case chịu trách nhiệm soft delete một Cinema.
 *
 * <p>Chịu trách nhiệm:
 * <ul>
 *     <li>Kiểm tra Cinema tồn tại theo ID</li>
 *     <li>Thực hiện soft delete bằng cách đánh dấu deletedAt</li>
 *     <li>Lưu thay đổi vào DB</li>
 * </ul>
 *
 * <p>Lưu ý:
 * <ul>
 *     <li>Ném exception nếu Cinema không tồn tại</li>
 *     <li>Không chứa logic liên quan đến presentation hay thao tác DB trực tiếp ngoài repository</li>
 * </ul>
 * @author Hieu Nguyen
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class DeleteCinemaUseCase {

    private final CinemaRepository cinemaRepository;

    /**
     * Xóa Cinema theo id.
     *
     * @param id của Cinema cần xóa
     */
    public void execute(CinemaId id) {
        if (!cinemaRepository.existsById(id)) {
            throw new IllegalArgumentException("Cinema with id: " + id + " not found!");
        }
        cinemaRepository.deleteById(id);
    }
}
