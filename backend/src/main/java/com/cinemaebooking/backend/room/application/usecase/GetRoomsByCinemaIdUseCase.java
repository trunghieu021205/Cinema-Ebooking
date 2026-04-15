package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.mapper.RoomResponseMapper;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * GetRoomsByCinemaIdUseCase: Use case chịu trách nhiệm lấy danh sách Room theo CinemaId.
 *
 * <p>Chịu trách nhiệm:
 * <ul>
 *     <li>Nhận cinemaId từ client</li>
 *     <li>Gọi Repository để lấy danh sách Room theo cinemaId (có pagination)</li>
 *     <li>Convert Domain -> DTO Response</li>
 * </ul>
 *
 * <p>Lưu ý:
 * <ul>
 *     <li>Không chứa logic liên quan đến presentation hay DB trực tiếp</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class GetRoomsByCinemaIdUseCase {

    private final RoomRepository roomRepository;
    private final RoomResponseMapper mapper;

    /**
     * Thực hiện lấy danh sách Room theo CinemaId.
     *
     * @param cinemaId id của cinema
     * @param pageable thông tin phân trang
     * @return Page<RoomResponse>
     */
    public Page<RoomResponse> execute(Long cinemaId, Pageable pageable) {

        // Validate input
        if (cinemaId == null) {
            throw new IllegalArgumentException("CinemaId must not be null");
        }

        // Gọi repository để lấy dữ liệu (có pagination)
        Page<Room> rooms = roomRepository.findByCinemaId(cinemaId, pageable);

        // Convert Domain -> DTO Response
        return rooms.map(mapper::toRoomResponse);
    }
}