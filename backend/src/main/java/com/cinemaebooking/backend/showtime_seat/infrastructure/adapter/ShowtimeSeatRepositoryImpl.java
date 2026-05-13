package com.cinemaebooking.backend.showtime_seat.infrastructure.adapter;

import com.cinemaebooking.backend.showtime_seat.application.port.ShowtimeSeatRepository;
import com.cinemaebooking.backend.showtime_seat.domain.enums.ShowtimeSeatStatus;
import com.cinemaebooking.backend.showtime_seat.domain.model.ShowtimeSeat;
import com.cinemaebooking.backend.showtime_seat.infrastructure.mapper.ShowtimeSeatMapperImpl;
import com.cinemaebooking.backend.showtime_seat.infrastructure.persistence.entity.ShowtimeSeatJpaEntity;
import com.cinemaebooking.backend.showtime_seat.infrastructure.persistence.repository.ShowtimeSeatJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ShowtimeSeatRepositoryImpl implements ShowtimeSeatRepository {

    private final ShowtimeSeatJpaRepository jpaRepository;
    private final ShowtimeSeatMapperImpl mapper;
    @Override
    public void saveAll(List<ShowtimeSeat> seats) {

        List<ShowtimeSeatJpaEntity> entities = seats.stream()
                .map(mapper::toEntity)
                .toList();

        jpaRepository.saveAll(entities);
    }

    @Override
    public List<ShowtimeSeat> findByShowtimeId(Long showtimeId) {

        return jpaRepository.findByShowtimeId(showtimeId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByShowtimeId(Long showtimeId) {

        List<ShowtimeSeatJpaEntity> entities =
                jpaRepository.findByShowtimeId(showtimeId);

        jpaRepository.deleteAll(entities);
    }


    @Override
    public void save(ShowtimeSeat showtimeSeat) {
        ShowtimeSeatJpaEntity entity = jpaRepository.findById(showtimeSeat.getId().getValue())
                .orElseThrow(() -> new RuntimeException("ShowtimeSeat not found: " + showtimeSeat.getId().getValue()));

        entity.setStatus(showtimeSeat.getStatus());

        jpaRepository.save(entity);
    }

    @Override
    public void updateStatusToAvailable(Long showtimeId, List<Long> showtimeSeatIds) {
        jpaRepository.updateStatusByShowtimeIdAndSeatIds(showtimeId, showtimeSeatIds, ShowtimeSeatStatus.AVAILABLE);
    }

    @Override
    public List<ShowtimeSeat> findAllByIds(List<Long> seatIds) {
        List<ShowtimeSeatJpaEntity> entities = jpaRepository.findAllById(seatIds);

        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
