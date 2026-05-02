package com.cinemaebooking.backend.loyalty.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@SuperBuilder
public class MembershipTier extends BaseEntity<MembershipTierId> {
    private String name;
    private BigDecimal minSpendingRequired;
    private BigDecimal discountPercent;
    private String benefitsDescription;
    private Integer tierLevel;

    public void update(String name, BigDecimal minSpending, BigDecimal discountPercent,
                       String benefitsDescription, Integer tierLevel) {
        this.name = name;
        this.minSpendingRequired = minSpending;
        this.discountPercent = discountPercent;
        this.benefitsDescription = benefitsDescription;
        this.tierLevel = tierLevel;
    }
}