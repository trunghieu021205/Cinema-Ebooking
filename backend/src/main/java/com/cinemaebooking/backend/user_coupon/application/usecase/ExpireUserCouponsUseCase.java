package com.cinemaebooking.backend.user_coupon.application.usecase;

import com.cinemaebooking.backend.user_coupon.application.port.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpireUserCouponsUseCase {

    private final UserCouponRepository userCouponRepository;

    @Transactional
    @Scheduled(cron = "0 0 2 * * ?") // 2h sáng mỗi ngày
    public void execute() {
        LocalDateTime now = LocalDateTime.now();
        var expiredList = userCouponRepository.findAvailableExpiredBefore(now);
        for (var coupon : expiredList) {
            coupon.expire();
            userCouponRepository.update(coupon);
        }
        log.info("Expired {} user coupons", expiredList.size());
    }
}