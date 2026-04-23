package com.cinemaebooking.backend.combo.application.usecase;

import com.cinemaebooking.backend.combo.application.port.ComboRepository;
import com.cinemaebooking.backend.combo.domain.valueobject.ComboId;
import com.cinemaebooking.backend.common.exception.domain.ComboExceptions;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteComboUseCase {

    private final ComboRepository comboRepository;

    public void execute(ComboId id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Combo id must not be null");
        }
        if (!comboRepository.existsById(id)) {
            throw ComboExceptions.notFound(id);
        }
        comboRepository.deleteById(id);
    }
}