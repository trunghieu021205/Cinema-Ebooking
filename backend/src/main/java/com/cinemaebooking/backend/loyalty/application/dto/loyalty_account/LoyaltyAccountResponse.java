package com.cinemaebooking.backend.loyalty.application.dto.loyalty_account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoyaltyAccountResponse {
    private Long loyaltyAccountId;
    private String totalPoints;
    private String totalSpending;
    private String tier;
}