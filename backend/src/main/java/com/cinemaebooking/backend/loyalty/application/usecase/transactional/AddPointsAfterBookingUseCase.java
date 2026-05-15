package com.cinemaebooking.backend.loyalty.application.usecase.transactional;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.exception_loyalty.LoyaltyExceptions;
import com.cinemaebooking.backend.loyalty.application.port.EarningRuleRepository;
import com.cinemaebooking.backend.loyalty.application.port.LoyaltyAccountRepository;
import com.cinemaebooking.backend.loyalty.application.port.LoyaltyTransactionRepository;
import com.cinemaebooking.backend.loyalty.application.port.MembershipTierRepository;
import com.cinemaebooking.backend.loyalty.domain.enums.EarningType;
import com.cinemaebooking.backend.loyalty.domain.enums.LoyaltyTransactionType;
import com.cinemaebooking.backend.loyalty.domain.model.EarningRule;
import com.cinemaebooking.backend.loyalty.domain.model.LoyaltyAccount;
import com.cinemaebooking.backend.loyalty.domain.model.LoyaltyTransaction;
import com.cinemaebooking.backend.loyalty.domain.model.MembershipTier;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.time.LocalDateTime;
@Slf4j
@Service
@RequiredArgsConstructor
public class AddPointsAfterBookingUseCase {
        private final LoyaltyAccountRepository loyaltyAccountRepository;
        private final EarningRuleRepository earningRuleRepository;
        private final MembershipTierRepository membershipTierRepository;
        private final LoyaltyTransactionRepository transactionRepository;

        @Transactional
        public void execute(Long userId, BigDecimal totalTicketPrice, BigDecimal totalComboPrice) {
            log.info("=== AddPointsAfterBooking === userId={}, totalTicketPrice={}, totalComboPrice={}", userId, totalTicketPrice, totalComboPrice);

            if (userId == null) {
                throw CommonExceptions.invalidInput("userId must not be null");
            }

            LoyaltyAccount account = loyaltyAccountRepository.findByUserId(userId)
                    .orElseThrow(() -> LoyaltyExceptions.notFoundByUserId(userId));

            log.info("Account found: id={}, currentPoints={}, tier={}", account.getId(), account.getCurrentPoints(), account.getTier());

            MembershipTier currentTier = account.getTier();
            BigDecimal pointsEarned = BigDecimal.ZERO;

            if (totalTicketPrice != null && totalTicketPrice.compareTo(BigDecimal.ZERO) > 0) {
                EarningRule ticketRule = findRuleForType(currentTier, EarningType.TICKET);
                log.info("Ticket rule: {}", ticketRule);
                BigDecimal rate = ticketRule != null ? ticketRule.getMultiplier() : BigDecimal.ZERO;
                log.info("Ticket rate={}", rate);
                BigDecimal base = totalTicketPrice.divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP);
                log.info("Ticket base={} (totalTicketPrice/1000)", base);
                pointsEarned = pointsEarned.add(base.multiply(rate));
                log.info("Ticket pointsEarned so far={}", pointsEarned);
            }

            if (totalComboPrice != null && totalComboPrice.compareTo(BigDecimal.ZERO) > 0) {
                EarningRule comboRule = findRuleForType(currentTier, EarningType.CONCESSION);
                log.info("Combo rule: {}", comboRule);
                BigDecimal rate = comboRule != null ? comboRule.getMultiplier() : BigDecimal.ZERO;
                log.info("Combo rate={}", rate);
                BigDecimal base = totalComboPrice.divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP);
                log.info("Combo base={}, comboPoints={}", base, base.multiply(rate));
                pointsEarned = pointsEarned.add(base.multiply(rate));
            }

            log.info("Total pointsEarned={}", pointsEarned);

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

            LoyaltyAccount saved = loyaltyAccountRepository.save(account);

            if (pointsEarned.compareTo(BigDecimal.ZERO) > 0) {
                LoyaltyTransaction transaction = LoyaltyTransaction.builder()
                        .loyaltyAccountId(saved.getId().getValue())
                        .type(LoyaltyTransactionType.EARN_FROM_BOOKING)
                        .changePoint(pointsEarned)
                        .balanceAfter(saved.getCurrentPoints())
                        .changeDate(LocalDateTime.now())
                        .build();
                transactionRepository.save(transaction);
            }
        }

        private EarningRule findRuleForType(MembershipTier tier, EarningType type) {
            List<EarningRule> rules = earningRuleRepository.findByTierAndType(
                    MembershipTierId.of(tier.getId().getValue()), type);
            log.info("findRuleForType: tier={} (id={}), type={}, rules found={}", tier.getName(), tier.getId(), type, rules.size());
            rules.forEach(r -> log.info("  rule: id={}, multiplier={}, active={}", r.getId(), r.getMultiplier(), r.getActive()));
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