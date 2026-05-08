package com.cinemaebooking.backend.loyalty.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.EarningRuleExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.MembershipTierExceptions;
import com.cinemaebooking.backend.loyalty.application.dto.earning_rule.CreateEarningRuleRequest;
import com.cinemaebooking.backend.loyalty.application.dto.earning_rule.UpdateEarningRuleRequest;
import com.cinemaebooking.backend.loyalty.application.port.EarningRuleRepository;
import com.cinemaebooking.backend.loyalty.application.port.MembershipTierRepository;
import com.cinemaebooking.backend.loyalty.domain.valueobject.EarningRuleId;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EarningRuleCommandValidator {
    private final MembershipTierRepository tierRepository;
    private final EarningRuleRepository ruleRepository;

    public void validateCreate(CreateEarningRuleRequest request) {
        if (request == null) throw CommonExceptions.invalidInput("Create request must not be null");
        validateBase(request.getEarningType(), request.getMultiplier(), request.getTierId());
        if (ruleRepository.existsDuplicate(MembershipTierId.of(request.getTierId()), request.getEarningType(), null))
            throw EarningRuleExceptions.duplicateRule();
    }

    public void validateUpdate(EarningRuleId id, UpdateEarningRuleRequest request) {
        if (id == null || request == null) throw CommonExceptions.invalidInput("Id and request must not be null");
        validateBase(request.getEarningType(), request.getMultiplier(), request.getTierId());
        if (ruleRepository.existsDuplicate(MembershipTierId.of(request.getTierId()), request.getEarningType(), id))
            throw EarningRuleExceptions.duplicateRule();
    }

    private void validateBase(String type, BigDecimal multiplier, Long tierId) {
        if (type == null || !Set.of("TICKET", "COMBO").contains(type))
            throw EarningRuleExceptions.invalidType(type);
        if (multiplier == null || multiplier.compareTo(BigDecimal.ZERO) <= 0)
            throw EarningRuleExceptions.invalidRate();
        if (tierId == null)
            throw CommonExceptions.invalidInput("Tier ID must not be null");
        if (!tierRepository.existsById(MembershipTierId.of(tierId)))
            throw MembershipTierExceptions.notFound(MembershipTierId.of(tierId));
    }
}