package com.cinemaebooking.backend.loyalty.application.port;

import com.cinemaebooking.backend.loyalty.domain.model.MembershipTier;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MembershipTierRepository {
    MembershipTier create(MembershipTier tier);
    MembershipTier update(MembershipTier tier);
    void deleteById(MembershipTierId id);
    Optional<MembershipTier> findById(MembershipTierId id);
    Page<MembershipTier> findAll(Pageable pageable);
    boolean existsById(MembershipTierId id);
    Optional<MembershipTier> findByName(String name);
    boolean existsByNameAndIdNot(String name, MembershipTierId id);
    boolean isUsedByAnyLoyaltyAccount(MembershipTierId id);
    List<MembershipTier> findAllByMinSpendingRequiredLessThanEqualOrderByTierLevelDesc(BigDecimal amount);
    List<MembershipTier> findAllOrderByTierLevelAsc();
}