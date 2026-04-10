package com.cinemaebooking.backend.cinema.presentation;

import com.cinemaebooking.backend.cinema.application.dto.CreateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.dto.UpdateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.usecase.*;
import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CinemaController: Presentation layer, chịu trách nhiệm expose API cho Cinema.
 *
 * <p>Chịu trách nhiệm:
 * <ul>
 *     <li>Nhận request từ client</li>
 *     <li>Gọi các UseCase tương ứng</li>
 *     <li>Trả response về client</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/cinemas")
@RequiredArgsConstructor
public class CinemaController {

    private final CreateCinemaUseCase createCinemaUseCase;
    private final UpdateCinemaUseCase updateCinemaUseCase;
    private final DeleteCinemaUseCase deleteCinemaUseCase;
    private final GetCinemaDetailUseCase getCinemaDetailUseCase;
    private final GetCinemaListUseCase getCinemaListUseCase;

    /**
     * POST /api/v1/cinemas
     *
     * <p>Endpoint tạo mới một Cinema.
     *
     * @param request dữ liệu tạo Cinema từ client
     * @return Cinema vừa được tạo
     */
    @PostMapping
    public ResponseEntity<Cinema> createCinema(@RequestBody CreateCinemaRequest request) {
        Cinema created = createCinemaUseCase.execute(request);
        return ResponseEntity.ok(created);
    }

    /**
     * PUT /api/v1/cinemas/{id}
     *
     * <p>Endpoint cập nhật thông tin một Cinema theo ID.
     *
     * @param id      ID của Cinema cần cập nhật
     * @param request dữ liệu cập nhật từ client
     * @return Cinema đã được cập nhật
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cinema> updateCinema(
            @PathVariable("id") Long id,
            @RequestBody UpdateCinemaRequest request) {
        Cinema updated = updateCinemaUseCase.execute(new CinemaId(id), request);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/v1/cinemas/{id}
     *
     * <p>Endpoint thực hiện soft delete một Cinema theo ID.
     *
     * @param id ID của Cinema cần xóa
     * @return ResponseEntity với status OK nếu thành công
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCinema(@PathVariable("id") Long id) {
        deleteCinemaUseCase.execute(new CinemaId(id));
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/v1/cinemas/{id}
     *
     * <p>Endpoint lấy chi tiết thông tin một Cinema theo ID.
     *
     * @param id ID của Cinema cần lấy thông tin
     * @return Cinema chi tiết
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cinema> getCinemaDetail(@PathVariable("id") Long id) {
        Cinema cinema = getCinemaDetailUseCase.execute(new CinemaId(id));
        return ResponseEntity.ok(cinema);
    }

    /**
     * GET /api/v1/cinemas
     *
     * <p>Endpoint lấy danh sách tất cả Cinema.
     *
     * @return Page<Cinema> danh sách Cinema
     */
    @GetMapping
    public ResponseEntity<Page<Cinema>> getCinemaList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return ResponseEntity.ok(getCinemaListUseCase.execute(page, size));
    }
}