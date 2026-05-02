package com.cinemaebooking.backend.loyalty.application.usecase.membership_tier;

import com.cinemaebooking.backend.loyalty.application.dto.membership_tier.CreateMembershipTierRequest;
import com.cinemaebooking.backend.loyalty.application.dto.membership_tier.MembershipTierResponse;
import com.cinemaebooking.backend.loyalty.application.mapper.MembershipTierResponseMapper;
import com.cinemaebooking.backend.loyalty.application.port.MembershipTierRepository;
import com.cinemaebooking.backend.loyalty.application.validator.MembershipTierCommandValidator;
import com.cinemaebooking.backend.loyalty.domain.model.MembershipTier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMembershipTierUseCase {
    private final MembershipTierRepository repository;
    private final MembershipTierResponseMapper mapper;
    private final MembershipTierCommandValidator validator;

    public MembershipTierResponse execute(CreateMembershipTierRequest request) {
        validator.validateCreate(request);
        MembershipTier tier = MembershipTier.builder()
                .name(request.getName())
                .minSpendingRequired(request.getMinSpendingRequired())
                .discountPercent(request.getDiscountPercent())
                .benefitsDescription(request.getBenefitsDescription())
                .tierLevel(request.getTierLevel())
                .build();
        return mapper.toResponse(repository.create(tier));
    }
}