package com.cinemaebooking.backend.loyalty.application.dto.membership_tier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMembershipTierRequest {
    private String name;
    private BigDecimal minSpendingRequired;
    private BigDecimal discountPercent;
    private String benefitsDescription;
    private Integer tierLevel;
}