package com.cinemaebooking.backend.cinema.application.usecase;

import com.cinemaebooking.backend.cinema.application.dto.CinemaResponse;
import com.cinemaebooking.backend.cinema.application.dto.CreateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.mapper.CinemaResponseMapper;
import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * CreateCinemaUseCase: Use case chịu trách nhiệm tạo mới một Cinema.
 *
 * <p>Chịu trách nhiệm:
 * <ul>
 *     <li>Nhận dữ liệu từ DTO</li>
 *     <li>Tạo domain object Cinema</li>
 *     <li>Gọi Repository để persist vào database</li>
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
public class CreateCinemaUseCase {

    private final CinemaRepository cinemaRepository;
    private final CinemaResponseMapper mapper;

    /**
     * Thực hiện tạo Cinema mới.
     *
     * @param request dữ liệu tạo Cinema từ client
     * @return Cinema đã được lưu trong database
     */
    public CinemaResponse execute(CreateCinemaRequest request) {

        // Tạo domain object Cinema
        Cinema cinema = Cinema.builder()
                .id(new CinemaId(null)) // id sẽ được DB auto-generate
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .status(CinemaStatus.ACTIVE) // mặc định active khi tạo mới
                .build();

        // Gọi repository để lưu
        Cinema savedCinema = cinemaRepository.save(cinema);


        // Convert Domain -> DTO Response
        return mapper.toResponse(savedCinema);
    }
}