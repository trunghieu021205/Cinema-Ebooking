package com.cinemaebooking.backend.loyalty.application.mapper;

import com.cinemaebooking.backend.loyalty.application.dto.loyalty_account.LoyaltyAccountResponse;
import com.cinemaebooking.backend.loyalty.domain.model.LoyaltyAccount;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyAccountResponseMapper {
    public LoyaltyAccountResponse toResponse(LoyaltyAccount account) {
        if (account == null) return null;
        return new LoyaltyAccountResponse(
                account.getId() != null ? account.getId().getValue() : null,
                account.getCurrentPoints().toPlainString(),
                account.getTotalSpending().toPlainString(),
                account.getTier() != null ? account.getTier().getName() : "Basic"
        );
    }
}