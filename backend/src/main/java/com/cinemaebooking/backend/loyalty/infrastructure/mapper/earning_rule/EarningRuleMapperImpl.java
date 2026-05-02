package com.cinemaebooking.backend.loyalty.infrastructure.mapper.earning_rule;

import com.cinemaebooking.backend.loyalty.domain.model.EarningRule;
import com.cinemaebooking.backend.loyalty.domain.valueobject.EarningRuleId;
import com.cinemaebooking.backend.loyalty.infrastructure.mapper.membership_tier.MembershipTierMapperImpl;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity.LoyaltyEarningRuleJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class EarningRuleMapperImpl implements EarningRuleMapper {

    @Override
    public LoyaltyEarningRuleJpaEntity toEntity(EarningRule domain) {
        if (domain == null) return null;
        return LoyaltyEarningRuleJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                // Không set membershipTier ở mapper – adapter sẽ gán managed entity
                .earningType(domain.getEarningType())
                .multiplier(domain.getMultiplier())
                .fixedPoints(domain.getFixedPoints())
                .description(domain.getDescription())
                .conditions(domain.getConditions())
                .active(domain.getActive())
                .priority(domain.getPriority())
                .build();
    }

    @Override
    public EarningRule toDomain(LoyaltyEarningRuleJpaEntity entity) {
        if (entity == null) return null;
        return EarningRule.builder()
                .id(EarningRuleId.ofNullable(entity.getId()))
                .tier(entity.getMembershipTier() != null ?
                        MembershipTierMapperImpl.staticToDomain(entity.getMembershipTier()) : null)
                .earningType(entity.getEarningType())
                .multiplier(entity.getMultiplier())
                .fixedPoints(entity.getFixedPoints())
                .description(entity.getDescription())
                .conditions(entity.getConditions())
                .active(entity.getActive())
                .priority(entity.getPriority())
                .build();
    }

    @Override
    public void updateEntity(LoyaltyEarningRuleJpaEntity entity, EarningRule domain) {
        if (entity == null || domain == null) return;
        entity.setEarningType(domain.getEarningType());
        entity.setMultiplier(domain.getMultiplier());
        entity.setFixedPoints(domain.getFixedPoints());
        entity.setDescription(domain.getDescription());
        entity.setConditions(domain.getConditions());
        entity.setActive(domain.getActive());
        entity.setPriority(domain.getPriority());
        // Không set membershipTier ở đây – adapter sẽ gán managed entity
    }
}