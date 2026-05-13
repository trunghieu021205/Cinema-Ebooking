package com.cinemaebooking.backend.loyalty.application.dto.membership_tier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MembershipTierResponse {
    private Long id;
    private String name;
    private BigDecimal minSpendingRequired;
    private BigDecimal discountPercent;
    private String benefitsDescription;
    private Integer tierLevel;
}