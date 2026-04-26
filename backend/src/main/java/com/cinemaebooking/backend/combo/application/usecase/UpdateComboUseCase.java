package com.cinemaebooking.backend.combo.application.usecase;

import com.cinemaebooking.backend.combo.application.dto.ComboResponse;
import com.cinemaebooking.backend.combo.application.dto.UpdateComboRequest;
import com.cinemaebooking.backend.combo.application.mapper.ComboResponseMapper;
import com.cinemaebooking.backend.combo.application.port.ComboRepository;
import com.cinemaebooking.backend.combo.application.validator.ComboCommandValidator;
import com.cinemaebooking.backend.combo.domain.model.Combo;
import com.cinemaebooking.backend.combo.domain.valueobject.ComboId;
import com.cinemaebooking.backend.common.exception.domain.ComboExceptions;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateComboUseCase {

    private final ComboRepository comboRepository;
    private final ComboResponseMapper mapper;
    private final ComboCommandValidator validator;

    public ComboResponse execute(ComboId id, UpdateComboRequest request) {
        validator.validateUpdateRequest(id, request);

        Combo combo = loadCombo(id);

        combo.update(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getOriginalPrice(),
                request.getImageUrl(),
                request.getStatus()
        );

        Combo saved = persist(combo);
        return mapper.toResponse(saved);
    }

    private Combo loadCombo(ComboId id) {
        return comboRepository.findById(id)
                .orElseThrow(() -> ComboExceptions.notFound(id));
    }

    private Combo persist(Combo combo) {
        try {
            return comboRepository.update(combo);
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
            throw CommonExceptions.concurrencyConflict();
        }
    }
}