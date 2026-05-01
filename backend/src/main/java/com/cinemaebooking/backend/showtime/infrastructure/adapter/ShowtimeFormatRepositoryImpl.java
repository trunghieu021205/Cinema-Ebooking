package com.cinemaebooking.backend.showtime.infrastructure.adapter;

import com.cinemaebooking.backend.showtime.application.port.ShowtimeFormatRepository;
import com.cinemaebooking.backend.showtime.domain.model.ShowtimeFormat;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeFormatId;
import com.cinemaebooking.backend.showtime.infrastructure.mappers.ShowtimeFormatMapper;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeFormatJpaEntity;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.repository.ShowtimeFormatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ShowtimeFormatRepositoryImpl implements ShowtimeFormatRepository {

    private final ShowtimeFormatJpaRepository jpaRepository;
    private final ShowtimeFormatMapper mapper;

    @Override
    public ShowtimeFormat create(ShowtimeFormat format) {
        var entity = mapper.toEntity(format);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public ShowtimeFormat update(ShowtimeFormat format) {

        ShowtimeFormatJpaEntity entity = jpaRepository.findByIdOrThrow(format.getId().getValue());
        // update field
        entity.setName(format.getName());
        entity.setExtraPrice(format.getExtraPrice());

        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(ShowtimeFormatId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public Optional<ShowtimeFormat> findById(ShowtimeFormatId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public Page<ShowtimeFormat> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, ShowtimeFormatId id) {
        return jpaRepository.existsByNameAndIdNot(name, id.getValue());
    }

    @Override
    public boolean existsById(ShowtimeFormatId id) {
        return jpaRepository.existsById(id.getValue());
    }
}
