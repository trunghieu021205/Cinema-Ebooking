package com.cinemaebooking.backend.cinema.infrastructure.adapter;

import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.cinema.infrastructure.mapper.CinemaMapper;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.entity.CinemaJpaEntity;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.repository.CinemaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * CinemaRepositoryImpl: Adapter triển khai CinemaRepository (port).
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Kết nối domain với database</li>
 *     <li>Sử dụng JpaRepository để thao tác DB</li>
 *     <li>Sử dụng Mapper để convert Domain ↔ JpaEntity</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Không chứa business logic</li>
 *     <li>Không expose JpaEntity ra ngoài</li>
 *     <li>Luôn convert qua mapper</li>
 * </ul>
 * @author Hieu Nguyen
 * @since 2026
 */
@Repository
@RequiredArgsConstructor
public class CinemaRepositoryImpl implements CinemaRepository {

    private final CinemaJpaRepository jpaRepository;
    private final CinemaMapper mapper;

    /**
     * Lưu Cinema mới vào database
     */
    @Override
    public Cinema create(Cinema cinema) {
        return mapper.toDomain(
                jpaRepository.save(mapper.toEntity(cinema))
        );
    }
    /**
     * Lưu Cinema đã được cập nhật vào database
     */
    @Override
    public Cinema update(Cinema cinema) {
        CinemaJpaEntity entity = jpaRepository.findById(cinema.getId().getValue())
                .orElseThrow(() -> new RuntimeException("Cinema not found"));

        mapper.updateEntity(entity, cinema);

        return mapper.toDomain(jpaRepository.save(entity));
    }

    /**
     * Tìm Cinema theo ID
     */
    @Override
    public Optional<Cinema> findById(CinemaId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    /**
     * Lấy toàn bộ danh sách Cinema từ database
     */
    @Override
    public Page<Cinema> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }
    /**
     * Xóa Cinema theo ID (soft delete)
     *
     * <p>
     * Chịu trách nhiệm:
     * <ul>
     *     <li>Đánh dấu Cinema là đã xóa thay vì xóa vật lý</li>
     *     <li>Đảm bảo dữ liệu không bị mất khỏi hệ thống</li>
     * </ul>
     *
     * <p>
     * Lưu ý:
     * <ul>
     *     <li>Ném exception nếu Cinema không tồn tại</li>
     * </ul>
     */
    @Override
    public void deleteById(CinemaId id) {
        var entity = jpaRepository.findById(id.getValue())
                .orElseThrow(() -> new RuntimeException("Cinema not found"));

        entity.setDeletedAt(LocalDateTime.now());
        jpaRepository.save(entity);
    }

    /**
     * Kiểm tra sự tồn tại của Cinema theo ID
     */
    @Override
    public boolean existsById(CinemaId id) {
        return jpaRepository.existsById(id.getValue());
    }

    /**
     * Kiểm tra sự tồn tại của Cinema theo name
     */
    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public Cinema findByName(String name) {
        CinemaJpaEntity entity = jpaRepository.findByName(name);
        return mapper.toDomain(entity);
    }
}
