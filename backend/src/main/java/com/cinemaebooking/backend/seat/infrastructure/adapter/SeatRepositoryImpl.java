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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final SeatJpaRepository seatJpaRepository;
    private final RoomJpaRepository roomJpaRepository;
    private final SeatTypeJpaRepository seatTypeJpaRepository;
    private final SeatMapper mapper;

    // CREATE
    @Override
    public Seat create(Seat seat) {

        SeatJpaEntity entity = mapper.toEntity(seat);

        if (seat.getRoomId() != null) {
            entity.setRoom(entityManager.getReference(RoomJpaEntity.class, seat.getRoomId()));
        }

        if (seat.getSeatTypeId() != null) {
            entity.setSeatType(entityManager.getReference(SeatTypeJpaEntity.class, seat.getSeatTypeId()));
        }

        SeatJpaEntity saved = seatJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    // UPDATE
    @Override
    public Seat update(Seat seat) {

        SeatJpaEntity old = seatJpaRepository.findByIdOrThrow(seat.getId().getValue());

        old.setStatus(seat.getStatus());

        if (seat.getSeatTypeId() != null) {
            old.setSeatType(entityManager.getReference(
                    SeatTypeJpaEntity.class,
                    seat.getSeatTypeId()
            ));
        }

        if (seat.getRoomId() != null) {
            old.setRoom(entityManager.getReference(
                    RoomJpaEntity.class,
                    seat.getRoomId()
            ));
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
    public boolean existsByRoomIdAndRowIndexAndColIndex(Long roomId, Integer rowIndex, Integer colIndex){
        return seatJpaRepository.existsByRoomIdAndRowIndexAndColIndex(roomId, rowIndex, colIndex);
    }

    // EXISTS CHECK (EXCLUDE ID)
    @Override
    public boolean existsByRoomIdAndRowIndexAndColIndexAndIdNot(Long roomId, Integer rowIndex, Integer colIndex, SeatId id){
        return seatJpaRepository.existsByRoomIdAndRowIndexAndColIndexAndIdNot(roomId, rowIndex, colIndex, id);
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

    @Override
    public List<Seat> findAllById(List<SeatId> ids) {
        List<Long> idValues = ids.stream()
                .map(SeatId::getValue)
                .toList();
        return seatJpaRepository.findAllById(idValues)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByRoomId(Long roomId) {
        return seatJpaRepository.existsByRoom_Id(roomId);
    }

    @Override
    @Transactional
    public void createBatch(List<Seat> seats) {

        if (seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException("Seat batch must not be empty");
        }

        Set<Long> roomIds = seats.stream()
                .map(Seat::getRoomId)
                .collect(Collectors.toSet());

        if (roomIds.size() != 1) {
            throw new IllegalArgumentException("All seats must belong to same room");
        }

        Long roomId = roomIds.iterator().next();
        RoomJpaEntity roomRef = entityManager.getReference(RoomJpaEntity.class, roomId);

        Map<Long, SeatTypeJpaEntity> typeCache = new HashMap<>();

        List<SeatJpaEntity> entities = seats.stream()
                .map(seat -> {
                    SeatJpaEntity entity = mapper.toEntity(seat);

                    entity.setRoom(roomRef);

                    if (seat.getSeatTypeId() != null) {
                        SeatTypeJpaEntity typeRef = typeCache.computeIfAbsent(
                                seat.getSeatTypeId(),
                                id -> entityManager.getReference(SeatTypeJpaEntity.class, id)
                        );
                        entity.setSeatType(typeRef);
                    }

                    return entity;
                })
                .toList();

        seatJpaRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public void updateBatch(List<Seat> seats) {

        List<Long> ids = seats.stream()
                .map(seat -> seat.getId().getValue())
                .toList();

        List<SeatJpaEntity> entities = seatJpaRepository.findAllById(ids);

        Map<Long, Seat> seatMap = seats.stream()
                .collect(Collectors.toMap(
                        seat -> seat.getId().getValue(),
                        seat -> seat
                ));

        for (SeatJpaEntity entity : entities) {

            Seat seat = seatMap.get(entity.getId());
            if (seat == null) continue;

            // update fields admin được chỉnh
            entity.setStatus(seat.getStatus());

            if (seat.getSeatTypeId() != null) {
                SeatTypeJpaEntity seatType = entityManager.getReference(
                        SeatTypeJpaEntity.class,
                        seat.getSeatTypeId()
                );
                entity.setSeatType(seatType);
            }
        }

        seatJpaRepository.saveAll(entities);
    }
}