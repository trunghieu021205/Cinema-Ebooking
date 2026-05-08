package com.cinemaebooking.backend.loyalty.infrastructure.mapper.earning_rule;

import com.cinemaebooking.backend.loyalty.domain.model.EarningRule;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity.LoyaltyEarningRuleJpaEntity;

public interface EarningRuleMapper {
    LoyaltyEarningRuleJpaEntity toEntity(EarningRule domain);
    EarningRule toDomain(LoyaltyEarningRuleJpaEntity entity);
    void updateEntity(LoyaltyEarningRuleJpaEntity entity, EarningRule domain);
}