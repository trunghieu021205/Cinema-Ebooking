package com.cinemaebooking.backend.loyalty.application.usecase.transactional;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.LoyaltyExceptions;
import com.cinemaebooking.backend.loyalty.application.port.EarningRuleRepository;
import com.cinemaebooking.backend.loyalty.application.port.LoyaltyAccountRepository;
import com.cinemaebooking.backend.loyalty.application.port.MembershipTierRepository;
import com.cinemaebooking.backend.loyalty.domain.model.EarningRule;
import com.cinemaebooking.backend.loyalty.domain.model.LoyaltyAccount;
import com.cinemaebooking.backend.loyalty.domain.model.MembershipTier;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddPointsAfterBookingUseCase {

    private final LoyaltyAccountRepository loyaltyAccountRepository;
    private final EarningRuleRepository earningRuleRepository;
    private final MembershipTierRepository membershipTierRepository;

    @Transactional
    public void execute(Long userId, BigDecimal totalTicketPrice, BigDecimal totalComboPrice) {
        // Input validation – phòng thủ
        if (userId == null) {
            throw CommonExceptions.invalidInput("userId must not be null");
        }

        LoyaltyAccount account = loyaltyAccountRepository.findByUserId(userId)
                .orElseThrow(() -> LoyaltyExceptions.notFoundByUserId(userId));

        MembershipTier currentTier = account.getTier();
        BigDecimal pointsEarned = BigDecimal.ZERO;

        // Tính điểm từ vé (nếu có)
        if (totalTicketPrice != null && totalTicketPrice.compareTo(BigDecimal.ZERO) > 0) {
            EarningRule ticketRule = findRuleForType(currentTier, "TICKET");
            BigDecimal rate = ticketRule != null ? ticketRule.getMultiplier() : BigDecimal.ZERO;
            BigDecimal base = totalTicketPrice.divide(new BigDecimal("100000"), 2, RoundingMode.HALF_UP);
            pointsEarned = pointsEarned.add(base.multiply(rate));
        }

        // Tính điểm từ combo
        if (totalComboPrice != null && totalComboPrice.compareTo(BigDecimal.ZERO) > 0) {
            EarningRule comboRule = findRuleForType(currentTier, "COMBO");
            BigDecimal rate = comboRule != null ? comboRule.getMultiplier() : BigDecimal.ZERO;
            BigDecimal base = totalComboPrice.divide(new BigDecimal("100000"), 2, RoundingMode.HALF_UP);
            pointsEarned = pointsEarned.add(base.multiply(rate));
        }

        // Cộng điểm
        account.setCurrentPoints(account.getCurrentPoints().add(pointsEarned));
        account.setLifetimePoints(account.getLifetimePoints().add(pointsEarned));

        // Cộng tổng chi tiêu (totalSpending)
        BigDecimal totalSpendingToAdd = (totalTicketPrice != null ? totalTicketPrice : BigDecimal.ZERO)
                .add(totalComboPrice != null ? totalComboPrice : BigDecimal.ZERO);
        account.addSpending(totalSpendingToAdd);

        // Re-evaluate tier
        MembershipTier newTier = evaluateTier(account.getTotalSpending());
        if (!newTier.getId().equals(account.getTier().getId())) {
            account.updateTier(newTier);
        }

        loyaltyAccountRepository.save(account);
    }

    private EarningRule findRuleForType(MembershipTier tier, String type) {
        List<EarningRule> rules = earningRuleRepository.findByTierAndType(
                MembershipTierId.of(tier.getId().getValue()), type);
        return rules.stream().filter(EarningRule::getActive).findFirst().orElse(null);
    }

    private MembershipTier evaluateTier(BigDecimal totalSpending) {
        // Lấy tier cao nhất có minSpending <= totalSpending
        return membershipTierRepository.findAllByMinSpendingRequiredLessThanEqualOrderByTierLevelDesc(totalSpending)
                .stream().findFirst()
                .orElseThrow(() -> CommonExceptions.resourceNotFound(
                        "No membership tier found for spending: " + totalSpending));
    }
}