package com.cinemaebooking.backend.loyalty.infrastructure.mapper.membership_tier;

import com.cinemaebooking.backend.loyalty.domain.model.MembershipTier;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity.MembershipTierJpaEntity;

public interface MembershipTierMapper {
    MembershipTierJpaEntity toEntity(MembershipTier domain);
    MembershipTier toDomain(MembershipTierJpaEntity entity);
    void updateEntity(MembershipTierJpaEntity entity, MembershipTier domain);
}