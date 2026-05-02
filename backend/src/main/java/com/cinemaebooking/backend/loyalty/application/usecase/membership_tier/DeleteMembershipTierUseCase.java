package com.cinemaebooking.backend.loyalty.application.usecase.membership_tier;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.MembershipTierExceptions;
import com.cinemaebooking.backend.loyalty.application.port.MembershipTierRepository;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteMembershipTierUseCase {
    private final MembershipTierRepository repository;

    public void execute(MembershipTierId id) {
        if (id == null) throw CommonExceptions.invalidInput("Id must not be null");
        if (!repository.existsById(id)) throw MembershipTierExceptions.notFound(id);
        if (repository.isUsedByAnyLoyaltyAccount(id)) throw MembershipTierExceptions.inUse();
        repository.deleteById(id);
    }
}