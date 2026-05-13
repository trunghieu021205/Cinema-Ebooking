package com.cinemaebooking.backend.loyalty.application.usecase.earning_rule;

import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.MembershipTierExceptions;
import com.cinemaebooking.backend.loyalty.application.dto.earning_rule.CreateEarningRuleRequest;
import com.cinemaebooking.backend.loyalty.application.dto.earning_rule.EarningRuleResponse;
import com.cinemaebooking.backend.loyalty.application.mapper.EarningRuleResponseMapper;
import com.cinemaebooking.backend.loyalty.application.port.EarningRuleRepository;
import com.cinemaebooking.backend.loyalty.application.port.MembershipTierRepository;
import com.cinemaebooking.backend.loyalty.application.validator.EarningRuleCommandValidator;
import com.cinemaebooking.backend.loyalty.domain.model.EarningRule;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateEarningRuleUseCase {
    private final EarningRuleRepository repository;
    private final MembershipTierRepository tierRepository;
    private final EarningRuleResponseMapper mapper;
    private final EarningRuleCommandValidator validator;

    @Transactional
    public EarningRuleResponse execute(CreateEarningRuleRequest request) {
        validator.validateCreate(request);
        var tier = tierRepository.findById(MembershipTierId.of(request.getTierId()))
                .orElseThrow(() -> MembershipTierExceptions.notFound(MembershipTierId.of(request.getTierId())));
        EarningRule rule = EarningRule.builder()
                .tier(tier)
                .earningType(request.getEarningType())
                .multiplier(request.getMultiplier())
                .fixedPoints(request.getFixedPoints())
                .description(request.getDescription())
                .conditions(request.getConditions())
                .active(request.getActive() != null ? request.getActive() : true)
                .priority(request.getPriority() != null ? request.getPriority() : 0)
                .build();
        return mapper.toResponse(repository.create(rule));
    }
}