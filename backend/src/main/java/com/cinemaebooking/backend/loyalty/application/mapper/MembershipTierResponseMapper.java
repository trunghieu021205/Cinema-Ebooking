package com.cinemaebooking.backend.loyalty.application.mapper;

import com.cinemaebooking.backend.loyalty.application.dto.membership_tier.MembershipTierResponse;
import com.cinemaebooking.backend.loyalty.domain.model.MembershipTier;
import org.springframework.stereotype.Component;

@Component
public class MembershipTierResponseMapper {
    public MembershipTierResponse toResponse(MembershipTier tier) {
        if (tier == null) return null;
        return new MembershipTierResponse(
                tier.getId().getValue(),
                tier.getName(),
                tier.getMinSpendingRequired(),
                tier.getDiscountPercent(),
                tier.getBenefitsDescription(),
                tier.getTierLevel()
        );
    }
}