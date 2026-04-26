package com.cinemaebooking.backend.user.infrastructure.mapper;

import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;
import com.cinemaebooking.backend.user.domain.model.User;
import com.cinemaebooking.backend.user.infrastructure.persistence.entity.UserJpaEntity;

public interface UserMapper extends BaseMapper<User, UserJpaEntity> {
}
