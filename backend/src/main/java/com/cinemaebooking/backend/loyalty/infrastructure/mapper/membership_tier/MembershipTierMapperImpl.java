package com.cinemaebooking.backend.loyalty.infrastructure.mapper.membership_tier;

import com.cinemaebooking.backend.loyalty.domain.model.MembershipTier;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity.MembershipTierJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class MembershipTierMapperImpl implements MembershipTierMapper {

    public static MembershipTier staticToDomain(MembershipTierJpaEntity entity) {
        if (entity == null) return null;
        return MembershipTier.builder()
                .id(MembershipTierId.ofNullable(entity.getId()))
                .name(entity.getName())
                .minSpendingRequired(entity.getMinSpendingRequired())
                .discountPercent(entity.getDiscountPercent())
                .benefitsDescription(entity.getBenefitsDescription())
                .tierLevel(entity.getTierLevel())
                .build();
    }

    @Override
    public MembershipTierJpaEntity toEntity(MembershipTier domain) {
        if (domain == null) return null;
        return MembershipTierJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .name(domain.getName())
                .minSpendingRequired(domain.getMinSpendingRequired())
                .discountPercent(domain.getDiscountPercent())
                .benefitsDescription(domain.getBenefitsDescription())
                .tierLevel(domain.getTierLevel())
                .build();
    }

    @Override
    public MembershipTier toDomain(MembershipTierJpaEntity entity) {
        return staticToDomain(entity);
    }

    @Override
    public void updateEntity(MembershipTierJpaEntity entity, MembershipTier domain) {
        entity.setName(domain.getName());
        entity.setMinSpendingRequired(domain.getMinSpendingRequired());
        entity.setDiscountPercent(domain.getDiscountPercent());
        entity.setBenefitsDescription(domain.getBenefitsDescription());
        entity.setTierLevel(domain.getTierLevel());
    }
}