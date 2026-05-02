package com.cinemaebooking.backend.loyalty.presentation;

import com.cinemaebooking.backend.loyalty.application.dto.membership_tier.CreateMembershipTierRequest;
import com.cinemaebooking.backend.loyalty.application.dto.membership_tier.MembershipTierResponse;
import com.cinemaebooking.backend.loyalty.application.dto.membership_tier.UpdateMembershipTierRequest;
import com.cinemaebooking.backend.loyalty.application.usecase.membership_tier.*;
import com.cinemaebooking.backend.loyalty.domain.valueobject.MembershipTierId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loyalty/membership-tiers")
@RequiredArgsConstructor
public class MembershipTierController {
    private final CreateMembershipTierUseCase create;
    private final UpdateMembershipTierUseCase update;
    private final DeleteMembershipTierUseCase delete;
    private final GetMembershipTierDetailUseCase detail;
    private final GetMembershipTierListUseCase list;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MembershipTierResponse create(@RequestBody CreateMembershipTierRequest request) {
        return create.execute(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public MembershipTierResponse update(@PathVariable Long id, @RequestBody UpdateMembershipTierRequest request) {
        return update.execute(MembershipTierId.of(id), request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        delete.execute(MembershipTierId.of(id));
    }

    @GetMapping("/{id}")
    public MembershipTierResponse detail(@PathVariable Long id) {
        return detail.execute(MembershipTierId.of(id));
    }

    @GetMapping
    public Page<MembershipTierResponse> list(@PageableDefault Pageable pageable) {
        return list.execute(pageable);
    }
}