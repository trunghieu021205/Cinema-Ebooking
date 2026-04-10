package com.cinemaebooking.backend.showtime.infrastructure.persistence.mapper;

import com.cinemaebooking.backend.showtime.domain.model.ShowtimeFormat;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeFormatId;
import com.cinemaebooking.backend.showtime.infrastructure.mapper.ShowtimeFormatMapperImpl;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeFormatJpaEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ShowtimeFormatMapperImplTest: Unit test cho ShowtimeFormatMapperImpl.
 *
 * <p>
 * Test các case:
 * <ul>
 *     <li>Domain → Entity</li>
 *     <li>Entity → Domain</li>
 *     <li>Null safety</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * Mapper này không có relationship nên test đơn giản, không cần mock.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
class ShowtimeFormatMapperImplTest {

    private final ShowtimeFormatMapperImpl mapper = new ShowtimeFormatMapperImpl();

    @Test
    void toEntity_shouldMapCorrectly() {
        // Arrange
        ShowtimeFormat domain = ShowtimeFormat.builder()
                .id(new ShowtimeFormatId(1L))
                .name("IMAX")
                .extraPrice(50000L)
                .build();

        // Act
        ShowtimeFormatJpaEntity entity = mapper.toEntity(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("IMAX", entity.getName());
        assertEquals(50000L, entity.getExtraPrice());
    }

    @Test
    void toDomain_shouldMapCorrectly() {
        // Arrange
        ShowtimeFormatJpaEntity entity = ShowtimeFormatJpaEntity.builder()
                .id(2L)
                .name("3D")
                .extraPrice(30000L)
                .build();

        // Act
        ShowtimeFormat domain = mapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertNotNull(domain.getId());
        assertEquals(2L, domain.getId().getValue());
        assertEquals("3D", domain.getName());
        assertEquals(30000L, domain.getExtraPrice());
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
