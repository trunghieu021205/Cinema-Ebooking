package com.cinemaebooking.backend.cinema.application.usecase;

import com.cinemaebooking.backend.cinema.application.dto.CreateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.dto.CinemaResponse;
import com.cinemaebooking.backend.cinema.application.mapper.CinemaResponseMapper;
import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.cinema.application.validator.CinemaCommandValidator;
import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.common.exception.domain.CinemaExceptions;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateCinemaUseCase - Unit Tests")
class CreateCinemaUseCaseTest {

    @Mock
    private CinemaRepository cinemaRepository;

    @Mock
    private CinemaResponseMapper mapper;

    @Mock
    private CinemaCommandValidator validator;

    @InjectMocks
    private CreateCinemaUseCase useCase;

    private CreateCinemaRequest request;
    private Cinema cinemaToSave;
    private Cinema savedCinema;
    private CinemaResponse expectedResponse;

    @BeforeEach
    void setUp() {
        // ================== REQUEST ==================
        request = new CreateCinemaRequest("CGV Aeon Mall Bình Dương",
                "01 Đại lộ Bình Dương",
                "Bình Dương");

        // ================== Cinema chưa có ID (gửi vào repository) ==================
        cinemaToSave = Cinema.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .status(CinemaStatus.ACTIVE)
                .build();

        // ================== Saved Cinema sau khi lưu (ID được sinh tự động) ==================
        savedCinema = Cinema.builder()
                .name(cinemaToSave.getName())
                .address(cinemaToSave.getAddress())
                .city(cinemaToSave.getCity())
                .status(CinemaStatus.ACTIVE)
                .build();

        // Set ID bằng factory method hợp lệ
        setCinemaId(savedCinema, 10L);

        // ================== EXPECTED RESPONSE ==================
        expectedResponse = new CinemaResponse(
                10L,
                savedCinema.getName(),
                savedCinema.getAddress(),
                savedCinema.getCity(),
                savedCinema.getStatus()
        );
    }

    /**
     * Helper method để set CinemaId an toàn vì constructor private
     */
    private void setCinemaId(Cinema cinema, Long idValue) {
        try {
            // Sử dụng factory method của CinemaId
            CinemaId cinemaId = CinemaId.of(idValue);

            // Set vào field id của BaseEntity (hoặc Cinema)
            var idField = Cinema.class.getSuperclass().getDeclaredField("id"); // vì kế thừa BaseEntity<CinemaId>
            idField.setAccessible(true);
            idField.set(cinema, cinemaId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set CinemaId in test using reflection", e);
        }
    }

    @Test
    @DisplayName("Should create cinema successfully - Happy Path")
    void shouldCreateCinemaSuccessfully() {
        // Given
        willDoNothing().given(validator).validateCreateRequest(request);
        given(cinemaRepository.create(any(Cinema.class))).willReturn(savedCinema);
        given(mapper.toResponse(savedCinema)).willReturn(expectedResponse);

        // When
        CinemaResponse result = useCase.execute(request);

        // Then
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getAddress(), result.getAddress());
        assertEquals(request.getCity(), result.getCity());
        assertEquals(CinemaStatus.ACTIVE, result.getStatus());

        // Verify interactions
        then(validator).should().validateCreateRequest(request);
        then(cinemaRepository).should().create(any(Cinema.class));
        then(mapper).should().toResponse(savedCinema);
    }

    @Test
    @DisplayName("Should throw exception when validation fails")
    void shouldThrowExceptionWhenValidationFails() {
        willThrow(CommonExceptions.invalidInput("Request must not be null"))
                .given(validator).validateCreateRequest(request);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.execute(request));

        assertTrue(ex.getMessage().contains("Request must not be null"));

        then(cinemaRepository).should(never()).create(any());
        then(mapper).should(never()).toResponse(any());
    }

    @Test
    @DisplayName("Should propagate exception when repository fails")
    void shouldThrowExceptionWhenRepositoryFails() {
        willDoNothing().given(validator).validateCreateRequest(request);
        given(cinemaRepository.create(any(Cinema.class)))
                .willThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> useCase.execute(request));

        then(validator).should().validateCreateRequest(request);
        then(mapper).should(never()).toResponse(any());
    }
}