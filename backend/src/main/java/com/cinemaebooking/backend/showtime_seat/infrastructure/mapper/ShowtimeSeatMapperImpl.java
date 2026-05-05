package com.cinemaebooking.backend.showtime_seat.infrastructure.mapper;

import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatJpaEntity;
import com.cinemaebooking.backend.seat.infrastructure.persistence.repository.SeatJpaRepository;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeJpaEntity;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.repository.ShowtimeJpaRepository;
import com.cinemaebooking.backend.showtime_seat.domain.enums.ShowtimeSeatStatus;
import com.cinemaebooking.backend.showtime_seat.domain.model.ShowtimeSeat;
import com.cinemaebooking.backend.showtime_seat.domain.valueobject.ShowtimeSeatId;
import com.cinemaebooking.backend.showtime_seat.infrastructure.persistence.entity.ShowtimeSeatJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShowtimeSeatMapperImpl implements ShowtimeSeatMapper {

    private final SeatJpaRepository seatJpaRepository;
    private final ShowtimeJpaRepository showtimeJpaRepository;

    @Override
    public ShowtimeSeatJpaEntity toEntity(ShowtimeSeat domain) {
        SeatJpaEntity seat = seatJpaRepository.getReferenceById(domain.getSeatId());

        ShowtimeJpaEntity showtime = showtimeJpaRepository.getReferenceById(domain.getShowtimeId());

        return ShowtimeSeatJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .seat(seat)
                .showtime(showtime)
                .status(domain.getStatus())
                .build();
    }

    @Override
    public ShowtimeSeat toDomain(ShowtimeSeatJpaEntity entity) {

        return ShowtimeSeat.builder()
                .id(ShowtimeSeatId.ofNullable(entity.getId()))
                .showtimeId(entity.getShowtime().getId())
                .seatId(entity.getSeat().getId())
                .build();
    }
}