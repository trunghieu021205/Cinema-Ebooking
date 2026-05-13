package com.cinemaebooking.backend.loyalty.application.usecase.earning_rule;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.EarningRuleExceptions;
import com.cinemaebooking.backend.loyalty.application.dto.earning_rule.EarningRuleResponse;
import com.cinemaebooking.backend.loyalty.application.mapper.EarningRuleResponseMapper;
import com.cinemaebooking.backend.loyalty.application.port.EarningRuleRepository;
import com.cinemaebooking.backend.loyalty.domain.valueobject.EarningRuleId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetEarningRuleDetailUseCase {
    private final EarningRuleRepository repository;
    private final EarningRuleResponseMapper mapper;

    @Transactional
    public EarningRuleResponse execute(EarningRuleId id) {
        if (id == null) throw CommonExceptions.invalidInput("Id must not be null");
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> EarningRuleExceptions.notFound(id));
    }
}