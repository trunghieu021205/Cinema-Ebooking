package com.cinemaebooking.backend.combo.application.validator;

import com.cinemaebooking.backend.combo.application.dto.CreateComboRequest;
import com.cinemaebooking.backend.combo.application.dto.UpdateComboRequest;
import com.cinemaebooking.backend.combo.application.port.ComboRepository;
import com.cinemaebooking.backend.combo.domain.valueobject.ComboId;
import com.cinemaebooking.backend.common.exception.domain.ComboExceptions;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ComboCommandValidator {

    private final ComboRepository comboRepository;

    // ================== CREATE ==================
    // trong method validateCreateRequest, thêm check status
    public void validateCreateRequest(CreateComboRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }

        validateFields(request.getName());

        if (comboRepository.existsByName(request.getName().trim())) {
            throw ComboExceptions.alreadyExists(request.getName());
        }
    }

    // ================== UPDATE ==================
    public void validateUpdateRequest(ComboId id, UpdateComboRequest request) {
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Combo id and request must not be null");
        }

        validateFields(request.getName());

        if (comboRepository.existsByNameAndIdNot(request.getName().trim(), id)) {
            throw ComboExceptions.alreadyExists(request.getName());
        }
    }

    private void validateFields(String name) {
        var profile = ValidationFactory.combo();
        ValidationEngine.of()
                .validate(name, "Combo name", profile.nameRules())
                .throwIfInvalid();
    }
}