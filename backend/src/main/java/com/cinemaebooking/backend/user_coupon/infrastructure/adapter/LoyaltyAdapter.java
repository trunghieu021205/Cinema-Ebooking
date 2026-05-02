package com.cinemaebooking.backend.user_coupon.infrastructure.adapter;

import com.cinemaebooking.backend.user_coupon.application.port.LoyaltyPort;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyAdapter implements LoyaltyPort {

    // TODO: Tiêm (Inject) Service hoặc Repository của module Loyalty vào đây
    // Ví dụ: private final LoyaltyAccountRepository loyaltyAccountRepository;

    @Override
    public int getUserPoints(Long userId) {
        // Tạm thời trả về 0.
        // Sau này thay bằng logic lấy điểm thực tế của user.
        return 0;
    }
}