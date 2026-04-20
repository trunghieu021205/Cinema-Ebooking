package com.cinemaebooking.backend.room.presentation;

import com.cinemaebooking.backend.room.application.dto.CreateRoomRequest;
import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.dto.UpdateRoomRequest;
import com.cinemaebooking.backend.room.application.usecase.*;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
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
    private final GetRoomListUseCase getRoomListUseCase;
    private final GetRoomsByCinemaIdUseCase getRoomsByCinemaIdUseCase;
    private final GetRoomByIdUseCase getRoomByIdUseCase;

    // ================== CREATE ==================

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @Valid @RequestBody CreateRoomRequest request
    ) {
        RoomResponse response = createRoomUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ================== GET BY ID ==================

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(
            @PathVariable Long id
    ) {
        RoomResponse response = getRoomByIdUseCase.execute(toRoomId(id));
        return ResponseEntity.ok(response);
    }

    // ================== GET ALL ==================

    @GetMapping
    public ResponseEntity<Page<RoomResponse>> getRooms(
            @PageableDefault(size = 8, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(getRoomListUseCase.execute(pageable));
    }

    // ================== UPDATE ==================

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @RequestBody UpdateRoomRequest request
    ) {
        RoomResponse response =
                updateRoomUseCase.execute(toRoomId(id), request);

        return ResponseEntity.ok(response);
    }

    // ================== DELETE ==================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long id
    ) {
        deleteRoomUseCase.execute(toRoomId(id));
        return ResponseEntity.ok().build();
    }

    // ================== ROOMS BY CINEMA ==================

    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<Page<RoomResponse>> getRoomsByCinema(
            @PathVariable Long cinemaId,
            @PageableDefault(size = 8, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(
                getRoomsByCinemaIdUseCase.execute(cinemaId, pageable)
        );
    }

    // ================== MAPPER HELPERS ==================

    private RoomId toRoomId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Room id must not be null");
        }
        return RoomId.of(id);
    }
}