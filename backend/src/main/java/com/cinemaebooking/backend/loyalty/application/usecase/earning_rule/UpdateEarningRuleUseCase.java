package com.cinemaebooking.backend.loyalty.application.usecase.earning_rule;

import org.springframework.transaction.annotation.Transactional;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.EarningRuleExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.MembershipTierExceptions;
import com.cinemaebooking.backend.loyalty.application.dto.earning_rule.EarningRuleResponse;
import com.cinemaebooking.backend.loyalty.application.dto.earning_rule.UpdateEarningRuleRequest;
import com.cinemaebooking.backend.loyalty.application.mapper.EarningRuleResponseMapper;
import com.cinemaebooking.backend.loyalty.application.port.EarningRuleRepository;
import com.cinemaebooking.backend.loyalty.application.port.MembershipTierRepository;
import com.cinemaebooking.backend.loyalty.application.validator.EarningRuleCommandValidator;
import com.cinemaebooking.backend.loyalty.domain.model.EarningRule;
import com.cinemaebooking.backend.loyalty.domain.valueobject.EarningRuleId;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateEarningRuleUseCase {
    private final EarningRuleRepository repository;
    private final MembershipTierRepository tierRepository;
    private final EarningRuleResponseMapper mapper;
    private final EarningRuleCommandValidator validator;

    @Transactional
    public EarningRuleResponse execute(EarningRuleId id, UpdateEarningRuleRequest request) {
        if (id == null || request == null) throw CommonExceptions.invalidInput("Id and request must not be null");
        validator.validateUpdate(id, request);
        EarningRule rule = repository.findById(id)
                .orElseThrow(() -> EarningRuleExceptions.notFound(id));
        var tier = tierRepository.findById(MembershipTierId.of(request.getTierId()))
                .orElseThrow(() -> MembershipTierExceptions.notFound(MembershipTierId.of(request.getTierId())));
        rule.update(tier, request.getEarningType(), request.getMultiplier(),
                request.getFixedPoints(), request.getDescription(), request.getConditions(),
                request.getActive(), request.getPriority());
        return mapper.toResponse(repository.update(rule));
    }
}