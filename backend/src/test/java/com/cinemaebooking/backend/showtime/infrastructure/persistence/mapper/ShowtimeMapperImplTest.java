package com.cinemaebooking.backend.showtime.infrastructure.persistence.mapper;

import com.cinemaebooking.backend.infrastructure.mapper.JpaReferenceHelper;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.MovieJpaEntity;
import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import com.cinemaebooking.backend.showtime.domain.enums.Language;
import com.cinemaebooking.backend.showtime.domain.model.Showtime;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import com.cinemaebooking.backend.showtime.infrastructure.mapper.ShowtimeMapperImpl;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeFormatJpaEntity;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeJpaEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ShowtimeMapperImplTest: Unit test cho ShowtimeMapperImpl.
 *
 * <p>
 * Test các case:
 * <ul>
 *     <li>Domain → Entity (có resolve relationship)</li>
 *     <li>Entity → Domain</li>
 *     <li>Null safety</li>
 * </ul>
 *
 * <p>
 * Vì mapper dùng EntityManager.getReference() nên cần mock EntityManager.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
class ShowtimeMapperImplTest {

    private ShowtimeMapperImpl mapper;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        mapper = new ShowtimeMapperImpl();
        entityManager = mock(EntityManager.class);

        injectEntityManager(mapper, entityManager);
    }

    /**
     * Inject EntityManager mock vào JpaReferenceHelper thông qua reflection.
     *
     * <p>
     * Lý do:
     * entityManager là protected field trong JpaReferenceHelper,
     * test không nằm cùng package nên không access trực tiếp được.
     */
    private void injectEntityManager(ShowtimeMapperImpl mapper, EntityManager em) {
        try {
            Field field = JpaReferenceHelper.class.getDeclaredField("entityManager");
            field.setAccessible(true);
            field.set(mapper, em);
        } catch (Exception e) {
            throw new RuntimeException("Cannot inject EntityManager into mapper", e);
        }
    }

    @Test
    void toEntity_shouldMapCorrectly_andResolveRelationship() {
        // Arrange
        Long movieId = 10L;
        Long roomId = 20L;
        Long formatId = 30L;

        MovieJpaEntity movieRef = MovieJpaEntity.builder().id(movieId).build();
        RoomJpaEntity roomRef = RoomJpaEntity.builder().id(roomId).build();
        ShowtimeFormatJpaEntity formatRef = ShowtimeFormatJpaEntity.builder().id(formatId).build();

        when(entityManager.getReference(MovieJpaEntity.class, movieId)).thenReturn(movieRef);
        when(entityManager.getReference(RoomJpaEntity.class, roomId)).thenReturn(roomRef);
        when(entityManager.getReference(ShowtimeFormatJpaEntity.class, formatId)).thenReturn(formatRef);

        Showtime domain = Showtime.builder()
                .id(new ShowtimeId(1L))
                .startTime(LocalDateTime.of(2026, 1, 1, 10, 0))
                .endTime(LocalDateTime.of(2026, 1, 1, 12, 0))
                .audioLanguage(Language.EN)
                .subtitleLanguage(Language.VI)
                .movieId(movieId)
                .roomId(roomId)
                .formatId(formatId)
                .build();

        // Act
        ShowtimeJpaEntity entity = mapper.toEntity(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(domain.getStartTime(), entity.getStartTime());
        assertEquals(domain.getEndTime(), entity.getEndTime());
        assertEquals(Language.EN, entity.getAudioLanguage());
        assertEquals(Language.VI, entity.getSubtitleLanguage());

        assertNotNull(entity.getMovie());
        assertNotNull(entity.getRoom());
        assertNotNull(entity.getFormat());

        assertEquals(movieId, entity.getMovie().getId());
        assertEquals(roomId, entity.getRoom().getId());
        assertEquals(formatId, entity.getFormat().getId());

        verify(entityManager, times(1)).getReference(MovieJpaEntity.class, movieId);
        verify(entityManager, times(1)).getReference(RoomJpaEntity.class, roomId);
        verify(entityManager, times(1)).getReference(ShowtimeFormatJpaEntity.class, formatId);
    }

    @Test
    void toDomain_shouldMapCorrectly() {
        // Arrange
        MovieJpaEntity movie = MovieJpaEntity.builder().id(100L).build();
        RoomJpaEntity room = RoomJpaEntity.builder().id(200L).build();
        ShowtimeFormatJpaEntity format = ShowtimeFormatJpaEntity.builder().id(300L).build();

        ShowtimeJpaEntity entity = ShowtimeJpaEntity.builder()
                .id(5L)
                .startTime(LocalDateTime.of(2026, 2, 2, 14, 0))
                .endTime(LocalDateTime.of(2026, 2, 2, 16, 0))
                .audioLanguage(Language.VI)
                .subtitleLanguage(Language.EN)
                .movie(movie)
                .room(room)
                .format(format)
                .build();

        // Act
        Showtime domain = mapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertNotNull(domain.getId());
        assertEquals(5L, domain.getId().getValue());

        assertEquals(entity.getStartTime(), domain.getStartTime());
        assertEquals(entity.getEndTime(), domain.getEndTime());
        assertEquals(Language.VI, domain.getAudioLanguage());
        assertEquals(Language.EN, domain.getSubtitleLanguage());

        assertEquals(100L, domain.getMovieId());
        assertEquals(200L, domain.getRoomId());
        assertEquals(300L, domain.getFormatId());
    }

    @Test
    void toEntity_shouldReturnNull_whenDomainIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDomain_shouldReturnNull_whenEntityIsNull() {
        assertNull(mapper.toDomain(null));
    }
}