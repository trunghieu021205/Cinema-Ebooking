package com.cinemaebooking.backend.loyalty.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.MembershipTierExceptions;
import com.cinemaebooking.backend.loyalty.application.dto.membership_tier.CreateMembershipTierRequest;
import com.cinemaebooking.backend.loyalty.application.dto.membership_tier.UpdateMembershipTierRequest;
import com.cinemaebooking.backend.loyalty.application.port.MembershipTierRepository;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class MembershipTierCommandValidator {
    private final MembershipTierRepository repository;

    public void validateCreate(CreateMembershipTierRequest request) {
        if (request == null) throw CommonExceptions.invalidInput("Create request must not be null");
        if (request.getName() == null || request.getName().trim().isEmpty())
            throw CommonExceptions.invalidInput("Name must not be blank");
        if (request.getMinSpendingRequired() == null || request.getMinSpendingRequired().compareTo(BigDecimal.ZERO) < 0)
            throw MembershipTierExceptions.invalidSpending();
        // Dùng findByName thay existsByName
        if (repository.findByName(request.getName()).isPresent())
            throw MembershipTierExceptions.duplicateName(request.getName());
    }

    public void validateUpdate(MembershipTierId id, UpdateMembershipTierRequest request) {
        if (id == null || request == null) throw CommonExceptions.invalidInput("Id and request must not be null");
        if (request.getName() == null || request.getName().trim().isEmpty())
            throw CommonExceptions.invalidInput("Name must not be blank");
        if (request.getMinSpendingRequired() == null || request.getMinSpendingRequired().compareTo(BigDecimal.ZERO) < 0)
            throw MembershipTierExceptions.invalidSpending();
        if (repository.existsByNameAndIdNot(request.getName(), id))
            throw MembershipTierExceptions.duplicateName(request.getName());
    }
}