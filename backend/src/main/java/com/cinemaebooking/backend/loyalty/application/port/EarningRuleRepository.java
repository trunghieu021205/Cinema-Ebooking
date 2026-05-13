package com.cinemaebooking.backend.loyalty.application.port;

import com.cinemaebooking.backend.loyalty.domain.enums.EarningType;
import com.cinemaebooking.backend.loyalty.domain.model.EarningRule;
import com.cinemaebooking.backend.loyalty.domain.valueobject.EarningRuleId;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import java.util.List;
import java.util.Optional;

public interface EarningRuleRepository {
    Optional<EarningRule> findById(EarningRuleId id);
    EarningRule create(EarningRule rule);
    EarningRule update(EarningRule rule);
    void deleteById(EarningRuleId id);
    List<EarningRule> findAll();
    List<EarningRule> findByTierAndType(MembershipTierId tierId, EarningType earningType);
    boolean existsById(EarningRuleId id);
    boolean existsDuplicate(MembershipTierId tierId, EarningType earningType, EarningRuleId excludeId);
    boolean existsByMembershipTierIdAndEarningType(
            Long membershipTierId,
            EarningType earningType
    );
}
