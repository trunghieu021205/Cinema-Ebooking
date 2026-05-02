package com.cinemaebooking.backend.loyalty.application.usecase.earning_rule;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.EarningRuleExceptions;
import com.cinemaebooking.backend.loyalty.application.port.EarningRuleRepository;
import com.cinemaebooking.backend.loyalty.domain.valueobject.EarningRuleId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteEarningRuleUseCase {
    private final EarningRuleRepository repository;

    @Transactional
    public void execute(EarningRuleId id) {
        if (id == null) throw CommonExceptions.invalidInput("Id must not be null");
        if (!repository.existsById(id)) throw EarningRuleExceptions.notFound(id);
        repository.deleteById(id);
    }
}