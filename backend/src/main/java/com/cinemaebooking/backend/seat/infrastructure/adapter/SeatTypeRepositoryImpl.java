package com.cinemaebooking.backend.seat.infrastructure.adapter;

import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.model.seatType.SeatType;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import com.cinemaebooking.backend.seat.infrastructure.mapper.seatType.SeatTypeMapper;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatTypeJpaEntity;
import com.cinemaebooking.backend.seat.infrastructure.persistence.repository.SeatTypeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

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
        SeatTypeJpaEntity entity = jpaRepository.findByIdOrThrow(seatType.getId().getValue());

        entity.setName(seatType.getName());
        entity.setBasePrice(seatType.getBasePrice());

        return mapper.toDomain(jpaRepository.save(entity));
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
        SeatTypeJpaEntity seatType = jpaRepository.findByIdOrThrow(id.getValue());
        jpaRepository.delete(seatType);
    }
    @Override
    public  boolean existsById(SeatTypeId id){
        return jpaRepository.existsById(id.getValue());
    }
    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, SeatTypeId id) {
        return jpaRepository.existsByNameAndIdNot(
                name,
                id.getValue()
        );
    }

    @Override
    public Optional<SeatType> findByNameIgnoreCase(String name) {
        return jpaRepository.findByNameIgnoreCase(name).map(mapper::toDomain);
    }
}
