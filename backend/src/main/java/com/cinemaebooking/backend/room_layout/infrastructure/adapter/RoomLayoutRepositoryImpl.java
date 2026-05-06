package com.cinemaebooking.backend.room_layout.infrastructure.adapter;

import com.cinemaebooking.backend.room_layout.application.port.roomLayout.RoomLayoutRepository;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayout.RoomLayoutId;
import com.cinemaebooking.backend.room_layout.infrastructure.mapper.roomLayout.RoomLayoutMapper;
import com.cinemaebooking.backend.room_layout.infrastructure.mapper.roomLayoutSeat.RoomLayoutSeatMapper;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.RoomLayoutJpaEntity;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.RoomLayoutSeatJpaEntity;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.repository.RoomLayoutJpaRepository;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.repository.RoomLayoutSeatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoomLayoutRepositoryImpl implements RoomLayoutRepository {

    private final RoomLayoutJpaRepository jpaRepository;
    private final RoomLayoutSeatJpaRepository seatJpaRepository;
    private final RoomLayoutMapper layoutMapper;
    private final RoomLayoutSeatMapper seatMapper;

    @Override
    @Transactional
    public RoomLayout create(RoomLayout layout) {
        RoomLayoutJpaEntity layoutEntity = layoutMapper.toEntity(layout);
        layoutEntity = jpaRepository.save(layoutEntity);
        Long layoutId = layoutEntity.getId();


        List<RoomLayoutSeatJpaEntity> seatEntities = layout.getSeats().stream()
                .map(seat -> {
                    RoomLayoutSeatJpaEntity entity = seatMapper.toEntity(seat);
                    entity.setRoomLayoutId(layoutId);
                    return entity;
                })
                .collect(Collectors.toList());
        seatJpaRepository.saveAll(seatEntities);

        return layoutMapper.toDomainWithSeats(layoutEntity, seatEntities);
    }

    @Override
    public Optional<RoomLayout> findById(RoomLayoutId id) {
        return jpaRepository.findById(id.getValue())
                .map(entity -> {
                    List<RoomLayoutSeatJpaEntity> seats = seatJpaRepository.findByRoomLayoutId(entity.getId());
                    return layoutMapper.toDomainWithSeats(entity, seats);
                });
    }

    @Override
    public Optional<RoomLayout> findCurrentByRoomIdAndDate(Long roomId, LocalDate date) {
        List<RoomLayoutJpaEntity> entities = jpaRepository.findCurrentByRoomIdAndDate(roomId, date);
        if (entities.isEmpty()) return Optional.empty();
        RoomLayoutJpaEntity entity = entities.get(0); // version cao nhất
        List<RoomLayoutSeatJpaEntity> seats = seatJpaRepository.findByRoomLayoutId(entity.getId());
        return Optional.of(layoutMapper.toDomainWithSeats(entity, seats));
    }

    @Override
    public Optional<RoomLayout> findLatestByRoomId(Long roomId) {
        return jpaRepository.findFirstByRoomIdOrderByLayoutVersionDesc(roomId)
                .map(entity -> {
                    List<RoomLayoutSeatJpaEntity> seats = seatJpaRepository.findByRoomLayoutId(entity.getId());
                    return layoutMapper.toDomainWithSeats(entity, seats);
                });
    }

    @Override
    public boolean existsByRoomId(Long roomId) {
        return jpaRepository.existsByRoomId(roomId);
    }

    @Override
    public List<RoomLayout> findAllByRoomIdOrderByLayoutVersionDesc(Long roomId) {
        return jpaRepository.findByRoomIdOrderByLayoutVersionDesc(roomId)
                .stream()
                .map(entity -> {
                    List<RoomLayoutSeatJpaEntity> seats = seatJpaRepository.findByRoomLayoutId(entity.getId());
                    return layoutMapper.toDomainWithSeats(entity, seats);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoomLayout> findByRoomIdAndLayoutVersion(Long roomId, Integer version) {
        return jpaRepository.findByRoomIdAndLayoutVersion(roomId, version)
                .map(entity -> {
                    List<RoomLayoutSeatJpaEntity> seats = seatJpaRepository.findByRoomLayoutId(entity.getId());
                    return layoutMapper.toDomainWithSeats(entity, seats);
                });
    }
}