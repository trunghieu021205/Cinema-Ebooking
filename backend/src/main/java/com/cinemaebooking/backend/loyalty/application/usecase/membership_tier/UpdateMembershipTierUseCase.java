package com.cinemaebooking.backend.loyalty.application.usecase.membership_tier;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.MembershipTierExceptions;
import com.cinemaebooking.backend.loyalty.application.dto.membership_tier.MembershipTierResponse;
import com.cinemaebooking.backend.loyalty.application.dto.membership_tier.UpdateMembershipTierRequest;
import com.cinemaebooking.backend.loyalty.application.mapper.MembershipTierResponseMapper;
import com.cinemaebooking.backend.loyalty.application.port.MembershipTierRepository;
import com.cinemaebooking.backend.loyalty.application.validator.MembershipTierCommandValidator;
import com.cinemaebooking.backend.loyalty.domain.model.MembershipTier;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateMembershipTierUseCase {
    private final MembershipTierRepository repository;
    private final MembershipTierResponseMapper mapper;
    private final MembershipTierCommandValidator validator;

    public MembershipTierResponse execute(MembershipTierId id, UpdateMembershipTierRequest request) {
        if (id == null || request == null) throw CommonExceptions.invalidInput("Id and request must not be null");
        validator.validateUpdate(id, request);
        MembershipTier tier = repository.findById(id)
                .orElseThrow(() -> MembershipTierExceptions.notFound(id));
        tier.update(request.getName(), request.getMinSpendingRequired(),
                request.getDiscountPercent(), request.getBenefitsDescription(), request.getTierLevel());
        return mapper.toResponse(repository.update(tier));
    }
}