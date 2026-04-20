package com.cinemaebooking.backend.bootstrap.reset;

import com.cinemaebooking.backend.room.infrastructure.persistence.repository.RoomJpaRepository;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.repository.CinemaJpaRepository;
import com.cinemaebooking.backend.seat.infrastructure.persistence.repository.SeatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResetService {
    private final RoomJpaRepository roomRepository;
    private final CinemaJpaRepository cinemaRepository;
    private final SeatJpaRepository seatRepository;

    @Transactional
    public void reset() {
        seatRepository.deleteAll();
        roomRepository.deleteAll();
        cinemaRepository.deleteAll();
    }
}



