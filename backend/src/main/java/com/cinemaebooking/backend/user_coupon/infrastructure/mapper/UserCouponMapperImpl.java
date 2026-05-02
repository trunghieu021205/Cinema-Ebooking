package com.cinemaebooking.backend.user_coupon.infrastructure.mapper;

import com.cinemaebooking.backend.user_coupon.domain.model.UserCoupon;
import com.cinemaebooking.backend.user_coupon.domain.valueobject.UserCouponId;
import com.cinemaebooking.backend.user_coupon.infrastructure.persistence.entity.UserCouponJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserCouponMapperImpl implements UserCouponMapper {

    @Override
    public UserCouponJpaEntity toEntity(UserCoupon domain) {
        if (domain == null) return null;
        return UserCouponJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .userId(domain.getUserId())
                .couponId(domain.getCouponId())
                .receivedAt(domain.getReceivedAt())
                .usageRemain(domain.getUsageRemain())
                .usedAt(domain.getUsedAt())
                .expiredAt(domain.getExpiredAt())
                .status(domain.getStatus())
                .build();
    }

    @Override
    public UserCoupon toDomain(UserCouponJpaEntity entity) {
        if (entity == null) return null;
        return UserCoupon.builder()
                .id(UserCouponId.ofNullable(entity.getId()))
                .userId(entity.getUserId())
                .couponId(entity.getCouponId())
                .receivedAt(entity.getReceivedAt())
                .usageRemain(entity.getUsageRemain())
                .usedAt(entity.getUsedAt())
                .expiredAt(entity.getExpiredAt())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public void updateEntity(UserCouponJpaEntity entity, UserCoupon domain) {
        if (entity == null || domain == null) return;
        entity.setUserId(domain.getUserId());
        entity.setCouponId(domain.getCouponId());
        entity.setReceivedAt(domain.getReceivedAt());
        entity.setUsageRemain(domain.getUsageRemain());
        entity.setUsedAt(domain.getUsedAt());
        entity.setExpiredAt(domain.getExpiredAt());
        entity.setStatus(domain.getStatus());
    }
}