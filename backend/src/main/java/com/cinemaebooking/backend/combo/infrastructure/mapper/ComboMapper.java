package com.cinemaebooking.backend.combo.infrastructure.mapper;

import com.cinemaebooking.backend.combo.domain.model.Combo;
import com.cinemaebooking.backend.combo.infrastructure.persistence.entity.ComboJpaEntity;
import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;

public interface ComboMapper extends BaseMapper<Combo, ComboJpaEntity> {

    void updateEntity(ComboJpaEntity entity, Combo domain);
}