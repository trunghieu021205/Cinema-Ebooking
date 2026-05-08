package com.cinemaebooking.backend.loyalty.infrastructure.adapter;

import com.cinemaebooking.backend.loyalty.application.port.MembershipTierRepository;
import com.cinemaebooking.backend.loyalty.domain.model.MembershipTier;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import com.cinemaebooking.backend.loyalty.infrastructure.mapper.membership_tier.MembershipTierMapper;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.entity.MembershipTierJpaEntity;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.repository.LoyaltyAccountJpaRepository;
import com.cinemaebooking.backend.loyalty.infrastructure.persistence.repository.MembershipTierJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MembershipTierRepositoryImpl implements MembershipTierRepository {

    private final MembershipTierJpaRepository jpaRepository;
    private final LoyaltyAccountJpaRepository accountJpaRepository;
    private final MembershipTierMapper mapper;

    @Override
    public MembershipTier create(MembershipTier tier) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(tier)));
    }

    @Override
    public MembershipTier update(MembershipTier tier) {
        MembershipTierJpaEntity entity = jpaRepository.findById(tier.getId().getValue()).orElseThrow();
        mapper.updateEntity(entity, tier);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(MembershipTierId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public Optional<MembershipTier> findById(MembershipTierId id) {
        return jpaRepository.findById(id.getValue())
                .filter(e -> e.getDeletedAt() == null)
                .map(mapper::toDomain);
    }

    @Override
    public Page<MembershipTier> findAll(Pageable pageable) {
        // Lấy toàn bộ tier đã sắp xếp, lọc những tier chưa bị xoá mềm
        List<MembershipTierJpaEntity> allEntities = jpaRepository.findAllByOrderByTierLevelAsc();
        List<MembershipTier> activeTiers = allEntities.stream()
                .filter(e -> e.getDeletedAt() == null)
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        // Phân trang thủ công
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), activeTiers.size());
        if (start >= activeTiers.size()) {
            return new PageImpl<>(List.of(), pageable, activeTiers.size());
        }
        List<MembershipTier> pageContent = activeTiers.subList(start, end);
        return new PageImpl<>(pageContent, pageable, activeTiers.size());
    }

    @Override
    public boolean existsById(MembershipTierId id) {
        return jpaRepository.findById(id.getValue())
                .filter(e -> e.getDeletedAt() == null)
                .isPresent();
    }

    @Override
    public Optional<MembershipTier> findByName(String name) {
        // Dùng findByName của JPA, lọc deletedAt
        return jpaRepository.findByName(name)
                .filter(e -> e.getDeletedAt() == null)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, MembershipTierId id) {
        // Dùng findByName rồi tự kiểm tra, vì existsByNameAndIdNot không lọc deletedAt
        return jpaRepository.findByName(name)
                .filter(e -> e.getDeletedAt() == null && !e.getId().equals(id.getValue()))
                .isPresent();
    }

    @Override
    public boolean isUsedByAnyLoyaltyAccount(MembershipTierId id) {
        return accountJpaRepository.findAll().stream()
                .anyMatch(acc -> acc.getMembershipTier() != null &&
                        acc.getMembershipTier().getId().equals(id.getValue()) &&
                        acc.getDeletedAt() == null);
    }

    @Override
    public List<MembershipTier> findAllByMinSpendingRequiredLessThanEqualOrderByTierLevelDesc(BigDecimal amount) {
        // Dùng findByMinSpendingRequiredLessThanEqualOrderByTierLevelDesc (có sẵn), lọc deletedAt
        return jpaRepository.findByMinSpendingRequiredLessThanEqualOrderByTierLevelDesc(amount)
                .stream()
                .filter(e -> e.getDeletedAt() == null)
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MembershipTier> findAllOrderByTierLevelAsc() {
        // Dùng findAllByOrderByTierLevelAsc (có sẵn), lọc deletedAt
        return jpaRepository.findAllByOrderByTierLevelAsc()
                .stream()
                .filter(e -> e.getDeletedAt() == null)
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}