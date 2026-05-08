package com.cinemaebooking.backend.loyalty.application.usecase.loyalty_account;

import com.cinemaebooking.backend.loyalty.application.port.LoyaltyAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteLoyaltyAccountUseCase {
    private final LoyaltyAccountRepository repository;

    @Transactional
    public void execute(Long userId) {
        repository.deleteByUserId(userId);
    }
}