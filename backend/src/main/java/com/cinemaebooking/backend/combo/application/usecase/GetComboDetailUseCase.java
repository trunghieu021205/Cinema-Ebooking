package com.cinemaebooking.backend.combo.application.usecase;

import com.cinemaebooking.backend.combo.application.dto.ComboResponse;
import com.cinemaebooking.backend.combo.application.mapper.ComboResponseMapper;
import com.cinemaebooking.backend.combo.application.port.ComboRepository;
import com.cinemaebooking.backend.combo.domain.valueobject.ComboId;
import com.cinemaebooking.backend.common.exception.domain.ComboExceptions;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetComboDetailUseCase {

    private final ComboRepository comboRepository;
    private final ComboResponseMapper mapper;

    public ComboResponse execute(ComboId id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Combo id must not be null");
        }
        return comboRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> ComboExceptions.notFound(id));
    }
}