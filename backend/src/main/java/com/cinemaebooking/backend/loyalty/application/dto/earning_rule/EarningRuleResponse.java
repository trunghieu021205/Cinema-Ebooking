package com.cinemaebooking.backend.loyalty.application.dto.earning_rule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class EarningRuleResponse {
    private Long id;
    private Long tierId;
    private String tierName;
    private String earningType;
    private BigDecimal multiplier;
    private BigDecimal fixedPoints;
    private String description;
    private String conditions;
    private Boolean active;
    private Integer priority;
}