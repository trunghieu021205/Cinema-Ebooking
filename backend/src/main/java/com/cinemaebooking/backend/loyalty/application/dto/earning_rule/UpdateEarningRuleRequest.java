package com.cinemaebooking.backend.loyalty.application.dto.earning_rule;

import com.cinemaebooking.backend.loyalty.domain.enums.EarningType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEarningRuleRequest {
    private Long tierId;
    private EarningType earningType;
    private BigDecimal multiplier;
    private BigDecimal fixedPoints;
    private String description;
    private String conditions;
    private Boolean active;
    private Integer priority;
}