package com.cinemaebooking.backend.cinema.application.usecase;

import com.cinemaebooking.backend.cinema.application.dto.CinemaResponse;
import com.cinemaebooking.backend.cinema.application.dto.UpdateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.mapper.CinemaResponseMapper;
import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * CreateCinemaUseCase: Use case chịu trách nhiệm cập nhật một Cinema.
 *
 * <p>Chịu trách nhiệm:
 * <ul>
 *     <li>Lấy Cinema hiện tại từ DB</li>
 *     <li>Cập nhật dữ liệu từ DTO</li>
 *     <li>Lưu thay đổi vào DB</li>
 * </ul>
 *
 * <p>Lưu ý:
 * <ul>
 *     <li>Không chứa logic liên quan đến presentation hay DB trực tiếp</li>
 * </ul>
 * @author Hieu Nguyen
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class UpdateCinemaUseCase {

    private final CinemaRepository cinemaRepository;
    private final CinemaResponseMapper mapper;

    /**
     * Thực hiện cập nhật Cinema theo id.
     *
     * @param id id của Cinema cần cập nhật
     * @param request dữ liệu cập nhật từ client
     * @return Cinema đã được cập nhật
     */

    public CinemaResponse execute(CinemaId id, UpdateCinemaRequest request) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema with id: " + id + " not found!"));
        // Update các field
        cinema.setName(request.getName());
        cinema.setAddress(request.getAddress());
        cinema.setCity(request.getCity());
        cinema.setStatus(request.getStatus());

        // Gọi repository để lưu
        Cinema savedCinema = cinemaRepository.update(cinema);


        // Convert Domain -> DTO Response
        return mapper.toResponse(savedCinema);
    }
}