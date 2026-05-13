package com.cinemaebooking.backend.loyalty.application.mapper;

import com.cinemaebooking.backend.loyalty.application.dto.earning_rule.EarningRuleResponse;
import com.cinemaebooking.backend.loyalty.domain.model.EarningRule;
import org.springframework.stereotype.Component;

@Component
public class EarningRuleResponseMapper {
    public EarningRuleResponse toResponse(EarningRule rule) {
        if (rule == null) return null;
        return new EarningRuleResponse(
                rule.getId().getValue(),
                rule.getTier() != null ? rule.getTier().getId().getValue() : null,
                rule.getTier() != null ? rule.getTier().getName() : null,
                rule.getEarningType(),
                rule.getMultiplier(),
                rule.getFixedPoints(),
                rule.getDescription(),
                rule.getConditions(),
                rule.getActive(),
                rule.getPriority()
        );
    }
}