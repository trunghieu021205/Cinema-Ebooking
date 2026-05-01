package com.cinemaebooking.backend.seat.infrastructure.adapter;

import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import com.cinemaebooking.backend.room.infrastructure.persistence.repository.RoomJpaRepository;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import com.cinemaebooking.backend.seat.infrastructure.mapper.seat.SeatMapper;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatJpaEntity;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatTypeJpaEntity;
import com.cinemaebooking.backend.seat.infrastructure.persistence.repository.SeatJpaRepository;
import com.cinemaebooking.backend.seat.infrastructure.persistence.repository.SeatTypeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;
    private final RoomJpaRepository roomJpaRepository;
    private final SeatTypeJpaRepository seatTypeJpaRepository;
    private final SeatMapper mapper;

    // CREATE
    @Override
    public Seat create(Seat seat) {

        SeatJpaEntity entity = mapper.toEntity(seat);

        // FIX: ROOM must be managed entity
        if (seat.getRoomId() != null) {
            RoomJpaEntity room = roomJpaRepository.findByIdOrThrow(seat.getRoomId());
            entity.setRoom(room);
        }

        // FIX: SEAT TYPE must be managed entity
        if (seat.getSeatTypeId() != null) {
            SeatTypeJpaEntity seatType = seatTypeJpaRepository.findByIdOrThrow(seat.getSeatTypeId());
            entity.setSeatType(seatType);
        }

        SeatJpaEntity saved = seatJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    // UPDATE
    @Override
    public Seat update(Seat seat) {

        SeatJpaEntity old = seatJpaRepository.findByIdOrThrow(seat.getId().getValue());

        old.setRowLabel(seat.getRowLabel());
        old.setColumnNumber(seat.getColumnNumber());
        old.setStatus(seat.getStatus());

        // update seat type safely
        if (seat.getSeatTypeId() != null) {
            SeatTypeJpaEntity seatType = seatTypeJpaRepository.findByIdOrThrow(seat.getSeatTypeId());
            old.setSeatType(seatType);
        }

        // optional update room
        if (seat.getRoomId() != null) {
            RoomJpaEntity room = roomJpaRepository.findByIdOrThrow(seat.getRoomId());
            old.setRoom(room);
        }

        return mapper.toDomain(seatJpaRepository.save(old));
    }

    @Override
    public boolean existsById(SeatId id){
        return seatJpaRepository.existsById(id.getValue());
    }
    // FIND BY ID
    @Override
    public Optional<Seat> findById(SeatId id) {
        return seatJpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    // FIND ALL
    @Override
    public Page<Seat> findAll(Pageable pageable) {
        return seatJpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }

    // DELETE (SOFT DELETE)
    @Override
    public void deleteById(SeatId id) {
        SeatJpaEntity seat = seatJpaRepository.findByIdOrThrow(id.getValue());
        seatJpaRepository.delete(seat);
    }

    // EXISTS CHECK
    @Override
    public boolean existsByRoomIdAndRowLabelAndColumnNumber(
            Long roomId, String rowLabel, Integer columnNumber) {

        return seatJpaRepository
                .existsByRoom_IdAndRowLabelAndColumnNumber(roomId, rowLabel, columnNumber);
    }

    // EXISTS CHECK (EXCLUDE ID)
    @Override
    public boolean existsByRoomIdAndRowLabelAndColumnNumberAndIdNot(
            Long roomId,
            String rowLabel,
            Integer columnNumber,
            SeatId id
    ){
        return seatJpaRepository.existsByRoom_IdAndRowLabelAndColumnNumberAndIdNot(
                roomId, rowLabel, columnNumber, id.getValue());
    }

    // FIND BY ROOM
    @Override
    public List<Seat> findByRoomId(Long roomId) {

        if (roomId == null) {
            throw new IllegalArgumentException("roomId must not be null");
        }

        return seatJpaRepository.findByRoom_Id(roomId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    public  List<Seat> findAllByIds(List<Long> ids){
        List<SeatJpaEntity> entities =
                seatJpaRepository.findAllById(ids);

        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }
}