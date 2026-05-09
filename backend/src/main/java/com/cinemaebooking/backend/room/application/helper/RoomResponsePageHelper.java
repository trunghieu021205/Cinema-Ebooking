package com.cinemaebooking.backend.room.application.helper;

import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.mapper.RoomResponseMapper;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room_layout.application.port.roomLayout.RoomLayoutRepository;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomResponsePageHelper {
    private final RoomLayoutRepository layoutRepository;
    private final RoomResponseMapper roomResponseMapper;

    public Page<RoomResponse> mapToResponsePage(Page<Room> roomPage, LocalDate date) {
        List<Room> rooms = roomPage.getContent();
        if (rooms.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), roomPage.getPageable(), roomPage.getTotalElements());
        }

        List<Long> roomIds = rooms.stream()
                .map(room -> room.getId().getValue())
                .collect(Collectors.toList());

        Map<Long, RoomLayout> layoutMap = layoutRepository.findCurrentByRoomIdsAndDate(roomIds, date)
                .stream()
                .collect(Collectors.toMap(RoomLayout::getRoomId, Function.identity()));

        List<RoomResponse> responses = rooms.stream()
                .map(room -> roomResponseMapper.toRoomResponse(room, layoutMap.get(room.getId().getValue())))
                .collect(Collectors.toList());

        return new PageImpl<>(responses, roomPage.getPageable(), roomPage.getTotalElements());
    }
}