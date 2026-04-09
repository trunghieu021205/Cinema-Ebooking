package com.cinemaebooking.backend.cinema.application.usecase;

import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GetCinemaListUseCase: Use case chịu trách nhiệm lấy danh sách tất cả Cinema.
 *
 * <p>Chịu trách nhiệm:
 * <ul>
 *     <li>Gọi Repository để lấy toàn bộ danh sách Cinema từ database</li>
 *     <li>Trả về danh sách các domain object Cinema</li>
 * </ul>
 *
 * <p>Lưu ý:
 * <ul>
 *     <li>Không chứa logic liên quan đến presentation hay thao tác database trực tiếp</li>
 *     <li>Chỉ trả domain object, không expose entity của JPA</li>
 * </ul>
 * @author Hieu Nguyen
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class GetCinemaListUseCase {

    private final CinemaRepository cinemaRepository;

    /**
     * Thực hiện lấy danh sách tất cả Cinema.
     *
     * @return danh sách domain object Cinema
     */
    public List<Cinema> execute() {
        return cinemaRepository.findAll();
    }
}