package com.cinemaebooking.backend.loyalty.application.port;

import com.cinemaebooking.backend.loyalty.domain.model.LoyaltyAccount;
import com.cinemaebooking.backend.loyalty.domain.valueobject.LoyaltyAccountId;
import java.util.Optional;

public interface LoyaltyAccountRepository {
    Optional<LoyaltyAccount> findById(LoyaltyAccountId id);
    Optional<LoyaltyAccount> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    LoyaltyAccount save(LoyaltyAccount account);
    void deleteByUserId(Long userId);
}