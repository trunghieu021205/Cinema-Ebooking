package com.cinemaebooking.backend.room.presentation;

import com.cinemaebooking.backend.room.application.usecase.*;
import com.cinemaebooking.backend.room.application.dto.CreateRoomRequest;
import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.dto.UpdateRoomRequest;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.room.infrastructure.persistence.repository.RoomJpaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final CreateRoomUseCase createRoomUseCase;
    private final UpdateRoomUseCase updateRoomUseCase;
    private final DeleteRoomUseCase deleteRoomUseCase;
    private final GetRoomUseCase getRoomUseCase;
    private final GetRoomsByCinemaIdUseCase getRoomsByCinemaIdUseCase;

    /**
     * POST /api/v1/rooms
     *
     * <p>Endpoint tạo mới một Room.
     *
     * @param request dữ liệu tạo Room từ client
     * @return Room vừa được tạo
     */
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        RoomResponse response = createRoomUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/v1/rooms/{id}
     *
     * <p>Endpoint lấy chi tiết thông tin một Room theo ID.
     *
     * @param id ID của Room cần lấy thông tin
     * @return Room chi tiết
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        RoomResponse response = getRoomUseCase.execute(new RoomId(id));
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/rooms
     *
     * <p>Endpoint lấy danh sách tất cả Room.
     *
     * @return Page<Room> danh sách Room
     */
    @GetMapping
    public ResponseEntity<Page<RoomResponse>> getRoomList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return ResponseEntity.ok(getRoomUseCase.execute(page, size));
    }

    /**
     * PUT /api/v1/rooms/{id}
     *
     * <p>Endpoint cập nhật thông tin một Room theo ID.
     *
     * @param id      ID của Room cần cập nhật
     * @param request dữ liệu cập nhật từ client
     * @return Room đã được cập nhật
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable("id") Long id,
            @RequestBody UpdateRoomRequest request) {
        return ResponseEntity.ok(updateRoomUseCase.execute(new RoomId(id), request));
    }

    /**
     * DELETE /api/v1/rooms/{id}
     *
     * <p>Endpoint thực hiện soft delete một Room theo ID.
     *
     * @param id ID của Room cần xóa
     * @return ResponseEntity với status OK nếu thành công
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        deleteRoomUseCase.execute(new RoomId(id));
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/v1/rooms/cinema/{cinemaId}
     *
     * <p>Endpoint lấy danh sách Room theo CinemaId (có phân trang).
     *
     * @param cinemaId ID của Cinema
     * @param pageable thông tin phân trang (page, size, sort)
     * @return ResponseEntity chứa danh sách Room thuộc Cinema
     */
    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<Page<RoomResponse>> getRoomsByCinema(
            @PathVariable Long cinemaId,
            @PageableDefault(size = 8, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(
                getRoomsByCinemaIdUseCase.execute(cinemaId, pageable)
        );
    }
}
