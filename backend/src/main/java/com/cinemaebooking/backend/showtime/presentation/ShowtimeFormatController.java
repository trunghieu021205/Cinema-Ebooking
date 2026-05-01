package com.cinemaebooking.backend.showtime.presentation;

import com.cinemaebooking.backend.showtime.application.dto.showtimeFormat.CreateShowtimeFormatRequest;
import com.cinemaebooking.backend.showtime.application.dto.showtimeFormat.ShowtimeFormatResponse;
import com.cinemaebooking.backend.showtime.application.dto.showtimeFormat.UpdateShowtimeFormatRequest;
import com.cinemaebooking.backend.showtime.application.usecase.showtimeFormat.*;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeFormatId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/showtime_formats")
@RequiredArgsConstructor
public class ShowtimeFormatController {

    private final CreateShowtimeFormatUsecase createUseCase;
    private final UpdateShowtimeFormatUsecase updateUseCase;
    private final DeleteShowtimeFormatUsecase deleteUseCase;
    private final GetShowtimeFormatListUsecase listUseCase;
    private final GetShowtimeFormatDetailUsecase detailUseCase;

    // ================== CREATE ==================

    @PostMapping
    public ShowtimeFormatResponse create(
            @RequestBody CreateShowtimeFormatRequest request
    ) {
        return createUseCase.execute(request);
    }

    // ================== UPDATE ==================

    @PutMapping("/{id}")
    public ShowtimeFormatResponse update(
            @PathVariable Long id,
            @RequestBody UpdateShowtimeFormatRequest request
    ) {
        return updateUseCase.execute(
                new ShowtimeFormatId(id),
                request
        );
    }

    // ================== DELETE ==================

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deleteUseCase.execute(new ShowtimeFormatId(id));
    }

    // ================== GET LIST ==================

    @GetMapping
    public Page<ShowtimeFormatResponse> getList(Pageable pageable) {
        return listUseCase.execute(pageable);
    }

    // ================== GET DETAIL ==================

    @GetMapping("/{id}")
    public ShowtimeFormatResponse getDetail(@PathVariable Long id) {
        return detailUseCase.execute(new ShowtimeFormatId(id));
    }
}
