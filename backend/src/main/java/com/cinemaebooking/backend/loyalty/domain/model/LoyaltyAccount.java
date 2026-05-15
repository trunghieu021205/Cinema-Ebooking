package com.cinemaebooking.backend.loyalty.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.loyalty.domain.enums.LoyaltyAccountStatus;
import com.cinemaebooking.backend.loyalty.domain.valueobject.LoyaltyAccountId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class LoyaltyAccount extends BaseEntity<LoyaltyAccountId> {
    private Long userId;
    private String loyaltyNumber;
    private BigDecimal totalSpending;
    private BigDecimal lifetimePoints;
    private BigDecimal currentPoints;
    private MembershipTier tier;
    private LocalDateTime lastActivityDate;
    private LocalDateTime joinedDate;
    private LoyaltyAccountStatus status;

    public void setCurrentPoints(BigDecimal currentPoints) { this.currentPoints = currentPoints; }
    public void setLifetimePoints(BigDecimal lifetimePoints) { this.lifetimePoints = lifetimePoints; }
    public void addSpending(BigDecimal amount) {
        this.totalSpending = this.totalSpending.add(amount);
        this.lastActivityDate = LocalDateTime.now();
    }
    public void deductPoints(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return;
        if (this.currentPoints.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient loyalty points");
        }
        this.currentPoints = this.currentPoints.subtract(amount);
        this.lastActivityDate = LocalDateTime.now();
    }
    public boolean hasEnoughPoints(BigDecimal requiredPoints) {
        return this.currentPoints.compareTo(requiredPoints) >= 0;
    }
    public void updateTier(MembershipTier newTier) { this.tier = newTier; }
    public void setStatus(LoyaltyAccountStatus status) { this.status = status; }
}