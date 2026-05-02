package com.cinemaebooking.backend.user_coupon.infrastructure.adapter;

import com.cinemaebooking.backend.user_coupon.application.port.UserCouponRepository;
import com.cinemaebooking.backend.user_coupon.domain.model.UserCoupon;
import com.cinemaebooking.backend.user_coupon.domain.valueobject.UserCouponId;
import com.cinemaebooking.backend.user_coupon.infrastructure.mapper.UserCouponMapper;
import com.cinemaebooking.backend.user_coupon.infrastructure.persistence.entity.UserCouponJpaEntity;
import com.cinemaebooking.backend.user_coupon.infrastructure.persistence.repository.UserCouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final UserCouponJpaRepository jpaRepository;
    private final UserCouponMapper mapper;

    @Override
    public UserCoupon create(UserCoupon userCoupon) {
        UserCouponJpaEntity entity = mapper.toEntity(userCoupon);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public UserCoupon update(UserCoupon userCoupon) {
        UserCouponJpaEntity entity = jpaRepository.findByIdOrThrow(userCoupon.getId().getValue());
        mapper.updateEntity(entity, userCoupon);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<UserCoupon> findById(UserCouponId id) {
        return jpaRepository.findById(id.getValue()).map(mapper::toDomain);
    }

    @Override
    public Page<UserCoupon> findByUserId(Long userId, Pageable pageable) {
        return jpaRepository.findByUserId(userId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByUserIdAndCouponId(Long userId, Long couponId) {
        return jpaRepository.existsByUserIdAndCouponId(userId, couponId);
    }
}