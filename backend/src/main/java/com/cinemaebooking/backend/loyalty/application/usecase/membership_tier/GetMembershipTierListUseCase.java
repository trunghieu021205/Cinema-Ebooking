package com.cinemaebooking.backend.loyalty.application.usecase.membership_tier;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.loyalty.application.dto.membership_tier.MembershipTierResponse;
import com.cinemaebooking.backend.loyalty.application.mapper.MembershipTierResponseMapper;
import com.cinemaebooking.backend.loyalty.application.port.MembershipTierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMembershipTierListUseCase {
    private final MembershipTierRepository repository;
    private final MembershipTierResponseMapper mapper;

    public Page<MembershipTierResponse> execute(Pageable pageable) {
        if (pageable == null) throw CommonExceptions.invalidInput("Pageable must not be null");
        return repository.findAll(pageable).map(mapper::toResponse);
    }
}