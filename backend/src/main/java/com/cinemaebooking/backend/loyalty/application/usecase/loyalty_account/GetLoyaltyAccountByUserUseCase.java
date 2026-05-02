package com.cinemaebooking.backend.loyalty.application.usecase.loyalty_account;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.LoyaltyExceptions;
import com.cinemaebooking.backend.loyalty.application.dto.loyalty_account.LoyaltyAccountResponse;
import com.cinemaebooking.backend.loyalty.application.mapper.LoyaltyAccountResponseMapper;
import com.cinemaebooking.backend.loyalty.application.port.LoyaltyAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetLoyaltyAccountByUserUseCase {
    private final LoyaltyAccountRepository repository;
    private final LoyaltyAccountResponseMapper mapper;

    public LoyaltyAccountResponse execute(Long userId) {
        if (userId == null) throw CommonExceptions.invalidInput("userId must not be null");
        return repository.findByUserId(userId)
                .map(mapper::toResponse)
                .orElseThrow(() -> LoyaltyExceptions.notFoundByUserId(userId));
    }
}