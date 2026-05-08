package com.cinemaebooking.backend.loyalty.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.loyalty.domain.valueobject.EarningRuleId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@SuperBuilder
public class EarningRule extends BaseEntity<EarningRuleId> {
    private MembershipTier tier;
    private String earningType;
    private BigDecimal multiplier;
    private BigDecimal fixedPoints;
    private String description;
    private String conditions;
    private Boolean active;
    private Integer priority;

    public void update(MembershipTier tier, String earningType, BigDecimal multiplier,
                       BigDecimal fixedPoints, String description, String conditions,
                       Boolean active, Integer priority) {
        this.tier = tier;
        this.earningType = earningType;
        this.multiplier = multiplier;
        this.fixedPoints = fixedPoints;
        this.description = description;
        this.conditions = conditions;
        this.active = active;
        this.priority = priority;
    }
}