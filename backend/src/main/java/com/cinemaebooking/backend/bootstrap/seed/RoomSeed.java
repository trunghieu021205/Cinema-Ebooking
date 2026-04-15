package com.cinemaebooking.backend.bootstrap.seed;

import com.cinemaebooking.backend.room.application.dto.CreateRoomRequest;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.enums.RoomStatus;
import com.cinemaebooking.backend.room.domain.enums.RoomType;
import com.cinemaebooking.backend.room.domain.model.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomSeed {

    private final RoomRepository roomRepository;

    private static final String ROOM_1 = "Room 1";
    private static final String ROOM_2 = "Room 2";
    private static final String ROOM_3 = "Room 3";

    public void seed(Long cinemaId) {

        if (cinemaId == null) {
            log.warn("Cannot seed rooms: cinemaId is null");
            return;
        }

        seedRoomIfNotExists(ROOM_1, 100, RoomType.TYPE_2D, cinemaId);
        seedRoomIfNotExists(ROOM_2, 80, RoomType.TYPE_3D, cinemaId);
        seedRoomIfNotExists(ROOM_3, 120, RoomType.IMAX, cinemaId);

        log.info("Room seeded successfully!");
    }

    private void seedRoomIfNotExists(String name, int totalSeats, RoomType type, Long cinemaId) {

        if (roomRepository.existsByNameAndCinemaId(name, cinemaId)) {
            return;
        }

        Room room = Room.builder()
                .name(name)
                .totalSeats(totalSeats)
                .roomType(type)
                .status(RoomStatus.ACTIVE)
                .cinemaId(cinemaId)
                .build();

        roomRepository.create(room);

        log.info("Room seeded: {}", name);
    }
}