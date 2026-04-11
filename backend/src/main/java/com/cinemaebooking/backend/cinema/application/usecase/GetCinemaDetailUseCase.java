package com.cinemaebooking.backend.cinema.application.usecase;

import com.cinemaebooking.backend.cinema.application.dto.CinemaResponse;
import com.cinemaebooking.backend.cinema.application.mapper.CinemaResponseMapper;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * GetCinemaDetailUseCase: Use case chịu trách nhiệm lấy chi tiết thông tin một Cinema theo ID.
 *
 * <p>Chịu trách nhiệm:
 * <ul>
 *     <li>Nhận ID của Cinema từ client</li>
 *     <li>Gọi Repository để tìm Cinema trong database</li>
 *     <li>Trả về domain object Cinema nếu tồn tại</li>
 * </ul>
 *
 * <p>Lưu ý:
 * <ul>
 *     <li>Không chứa logic liên quan đến presentation hay thao tác database trực tiếp</li>
 *     <li>Chưa xử lý exception nếu Cinema không tồn tại, có thể bổ sung sau</li>
 * </ul>
 * @author Hieu Nguyen
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class GetCinemaDetailUseCase {

    private final CinemaRepository cinemaRepository;
    private final CinemaResponseMapper mapper;

    /**
     * Thực hiện lấy chi tiết Cinema theo ID.
     *
     * @param id ID của Cinema cần lấy
     * @return Cinema domain object nếu tồn tại, null nếu chưa xử lý exception
     */
    public CinemaResponse execute(CinemaId id) {
        return cinemaRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Cinema with id: " + id + " not found!"));
    }
}
