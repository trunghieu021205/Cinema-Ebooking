package com.cinemaebooking.backend.loyalty.infrastructure.adapter;

import com.cinemaebooking.backend.loyalty.application.port.EarningRuleRepository;
import com.cinemaebooking.backend.loyalty.domain.model.EarningRule;
import com.cinemaebooking.backend.loyalty.domain.valueobject.EarningRuleId;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import com.cinemaebooking.backend.loyalty.infrastructure.mapper.earning_rule.EarningRuleMapper;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity.LoyaltyEarningRuleJpaEntity;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity.MembershipTierJpaEntity;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.repository.LoyaltyEarningRuleJpaRepository;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.repository.MembershipTierJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EarningRuleRepositoryImpl implements EarningRuleRepository {

    private final LoyaltyEarningRuleJpaRepository jpaRepository;
    private final MembershipTierJpaRepository tierJpaRepository;
    private final EarningRuleMapper mapper;

    @Override
    public Optional<EarningRule> findById(EarningRuleId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public EarningRule create(EarningRule rule) {
        LoyaltyEarningRuleJpaEntity entity = mapper.toEntity(rule);
        MembershipTierJpaEntity tierRef = tierJpaRepository.getReferenceById(
                rule.getTier().getId().getValue());
        entity.setMembershipTier(tierRef);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public EarningRule update(EarningRule rule) {
        LoyaltyEarningRuleJpaEntity entity = jpaRepository.findById(rule.getId().getValue())
                .orElseThrow(() -> new RuntimeException("Earning rule not found"));
        mapper.updateEntity(entity, rule);
        MembershipTierJpaEntity tierRef = tierJpaRepository.getReferenceById(
                rule.getTier().getId().getValue());
        entity.setMembershipTier(tierRef);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(EarningRuleId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public List<EarningRule> findAll() {
        // Sử dụng method có sẵn: findByActiveTrue (chỉ lấy rule active)
        return jpaRepository.findByActiveTrue()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<EarningRule> findByTierAndType(MembershipTierId tierId, String earningType) {
        MembershipTierJpaEntity tierRef = tierJpaRepository.getReferenceById(tierId.getValue());
        // Sử dụng method có sẵn: findByMembershipTierAndEarningTypeAndActiveTrueOrderByPriorityDesc
        return jpaRepository.findByMembershipTierAndEarningTypeAndActiveTrueOrderByPriorityDesc(
                        tierRef, earningType)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(EarningRuleId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public boolean existsDuplicate(MembershipTierId tierId, String earningType, EarningRuleId excludeId) {
        MembershipTierJpaEntity tierRef = tierJpaRepository.getReferenceById(tierId.getValue());
        // Sử dụng method có sẵn: findByMembershipTierAndEarningType (tìm tất cả, không lọc active/deleted)
        List<LoyaltyEarningRuleJpaEntity> list = jpaRepository
                .findByMembershipTierAndEarningType(tierRef, earningType);

        if (excludeId == null) {
            return !list.isEmpty();
        }
        return list.stream().anyMatch(e -> !e.getId().equals(excludeId.getValue()));
    }
}