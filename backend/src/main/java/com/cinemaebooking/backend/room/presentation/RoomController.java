package com.cinemaebooking.backend.room.presentation;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.room.application.dto.CreateRoomRequest;
import com.cinemaebooking.backend.room.application.dto.RoomLayoutResponse;
import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.dto.UpdateRoomRequest;
import com.cinemaebooking.backend.room.application.usecase.*;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RoomController - REST API for Room resource.
 * Responsibility:
 * - Handle HTTP requests
 * - Delegate to use cases
 * - Return proper HTTP responses
 */
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
    private final GenerateRoomLayoutUseCase generateRoomLayoutUseCase;
    private final GetRoomLayoutUseCase getRoomLayoutUseCase;

    // ================== CREATE ==================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse createRoom(@Valid @RequestBody CreateRoomRequest request) {
        return createRoomUseCase.execute(request);
    }

    // ================== UPDATE ==================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoomResponse updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoomRequest request) {

        RoomId roomId = toRoomId(id);
        return updateRoomUseCase.execute(roomId, request);
    }

    // ================== DELETE ==================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable Long id) {

        RoomId roomId = toRoomId(id);
        deleteRoomUseCase.execute(roomId);
    }

    // ================== DETAIL ==================
    @GetMapping("/{id}")
    public RoomResponse getRoomById(@PathVariable Long id) {

        RoomId roomId = toRoomId(id);
        return getRoomByIdUseCase.execute(roomId);
    }

    // ================== LIST ==================
    @GetMapping
    public Page<RoomResponse> getRoomList(
            @PageableDefault(size = 8, page = 0) Pageable pageable) {

        return getRoomListUseCase.execute(pageable);
    }

    // ================== LIST BY CINEMA ==================
    @GetMapping("/cinema/{cinemaId}")
    public Page<RoomResponse> getRoomsByCinema(
            @PathVariable Long cinemaId,
            @PageableDefault(size = 8, page = 0) Pageable pageable) {

        return getRoomsByCinemaIdUseCase.execute(cinemaId, pageable);
    }

    @PostMapping("/{id}/generate-layout")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void generateLayout(@PathVariable Long id) {
        generateRoomLayoutUseCase.execute(toRoomId(id));
    }

    @GetMapping("/{id}/layout")
    public RoomLayoutResponse getLayout(@PathVariable Long id) {
        return getRoomLayoutUseCase.execute(id);
    }

    // ================== HELPER ==================
    private RoomId toRoomId(Long id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Room id must not be null");
        }
        return RoomId.of(id);
    }
}