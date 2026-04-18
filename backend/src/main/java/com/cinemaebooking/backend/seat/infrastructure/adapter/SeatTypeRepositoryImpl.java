package com.cinemaebooking.backend.seat.infrastructure.adapter;

import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.model.seatType.SeatType;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import com.cinemaebooking.backend.seat.infrastructure.mapper.seatType.SeatTypeMapper;
import com.cinemaebooking.backend.seat.infrastructure.persistence.repository.SeatTypeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SeatTypeRepositoryImpl implements SeatTypeRepository {

    private final SeatTypeJpaRepository jpaRepository;
    private final SeatTypeMapper mapper;

    @Override
    public SeatType create(SeatType seatType) {
        var entity = mapper.toEntity(seatType);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public SeatType update(SeatType seatType) {
        var old = jpaRepository.findById(seatType.getId().getValue())
                .orElseThrow(() -> new RuntimeException("SeatType not found"));

        old.setName(seatType.getName());
        old.setBasePrice(seatType.getBasePrice());

        return mapper.toDomain(jpaRepository.save(old));
    }

    @Override
    public Optional<SeatType> findById(SeatTypeId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public Page<SeatType> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(SeatTypeId id) {
        var entity = jpaRepository.findById(id.getValue())
                .orElseThrow(() -> new RuntimeException("SeatType not found"));

        entity.setDeletedAt(LocalDateTime.now());
        jpaRepository.save(entity);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }
}
