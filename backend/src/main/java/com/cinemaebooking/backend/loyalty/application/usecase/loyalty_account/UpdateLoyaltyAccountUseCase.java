package com.cinemaebooking.backend.loyalty.application.usecase.loyalty_account;

import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.LoyaltyExceptions;
import com.cinemaebooking.backend.loyalty.application.port.LoyaltyAccountRepository;
import com.cinemaebooking.backend.loyalty.domain.enums.LoyaltyAccountStatus;
import com.cinemaebooking.backend.loyalty.domain.model.LoyaltyAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateLoyaltyAccountUseCase {
    private final LoyaltyAccountRepository repository;

    @Transactional
    public void updateStatus(Long userId, LoyaltyAccountStatus newStatus) {
        LoyaltyAccount account = repository.findByUserId(userId)
                .orElseThrow(() -> LoyaltyExceptions.notFoundByUserId(userId));
        account.setStatus(newStatus);
        repository.save(account);
    }
}