package com.cinemaebooking.backend.loyalty.application.usecase.membership_tier;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.MembershipTierExceptions;
import com.cinemaebooking.backend.loyalty.application.dto.membership_tier.MembershipTierResponse;
import com.cinemaebooking.backend.loyalty.application.mapper.MembershipTierResponseMapper;
import com.cinemaebooking.backend.loyalty.application.port.MembershipTierRepository;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMembershipTierDetailUseCase {
    private final MembershipTierRepository repository;
    private final MembershipTierResponseMapper mapper;

    public MembershipTierResponse execute(MembershipTierId id) {
        if (id == null) throw CommonExceptions.invalidInput("Id must not be null");
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> MembershipTierExceptions.notFound(id));
    }
}