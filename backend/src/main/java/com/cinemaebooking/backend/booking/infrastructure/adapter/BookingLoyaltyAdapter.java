package com.cinemaebooking.backend.booking.infrastructure.adapter;

import com.cinemaebooking.backend.booking.application.port.BookingLoyaltyPort;
import com.cinemaebooking.backend.loyalty.application.port.LoyaltyAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class BookingLoyaltyAdapter implements BookingLoyaltyPort {

    private final LoyaltyAccountRepository loyaltyAccountRepository;

    @Override
    public MembershipTierInfo getMembershipTierInfo(Long userId) {
        if (userId == null) return new MembershipTierInfo(null, "Basic", BigDecimal.ZERO);

        return loyaltyAccountRepository.findByUserId(userId)
                .map(account -> {
                    var tier = account.getTier();
                    if (tier == null) return new MembershipTierInfo(null, "Basic", BigDecimal.ZERO);
                    return new MembershipTierInfo(
                            tier.getId().getValue(),
                            tier.getName(),
                            tier.getDiscountPercent() != null ? tier.getDiscountPercent() : BigDecimal.ZERO
                    );
                })
                .orElse(new MembershipTierInfo(null, "Basic", BigDecimal.ZERO));
    }
}
