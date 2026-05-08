package com.cinemaebooking.backend.loyalty.application.usecase.earning_rule;

import com.cinemaebooking.backend.loyalty.application.dto.earning_rule.EarningRuleResponse;
import com.cinemaebooking.backend.loyalty.application.mapper.EarningRuleResponseMapper;
import com.cinemaebooking.backend.loyalty.application.port.EarningRuleRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetEarningRuleListUseCase {
    private final EarningRuleRepository repository;
    private final EarningRuleResponseMapper mapper;

    @Transactional
    public List<EarningRuleResponse> execute() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }
}