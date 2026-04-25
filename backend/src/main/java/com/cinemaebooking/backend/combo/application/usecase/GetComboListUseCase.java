package com.cinemaebooking.backend.combo.application.usecase;

import com.cinemaebooking.backend.combo.application.dto.ComboResponse;
import com.cinemaebooking.backend.combo.application.mapper.ComboResponseMapper;
import com.cinemaebooking.backend.combo.application.port.ComboRepository;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetComboListUseCase {

    private final ComboRepository comboRepository;
    private final ComboResponseMapper mapper;

    public Page<ComboResponse> execute(Pageable pageable) {
        if (pageable == null) {
            throw CommonExceptions.invalidInput("Pageable must not be null");
        }
        return comboRepository.findAll(pageable)
                .map(mapper::toResponse);
    }
}