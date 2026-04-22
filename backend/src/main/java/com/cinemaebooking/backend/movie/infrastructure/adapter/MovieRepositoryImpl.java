package com.cinemaebooking.backend.movie.infrastructure.adapter;

import com.cinemaebooking.backend.movie.application.port.MovieRepository;
import com.cinemaebooking.backend.movie.domain.model.Movie;
import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import com.cinemaebooking.backend.movie.infrastructure.mapper.MovieMapper;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.GenreJpaEntity;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.MovieJpaEntity;
import com.cinemaebooking.backend.movie.infrastructure.persistence.repository.GenreJpaRepository;
import com.cinemaebooking.backend.movie.infrastructure.persistence.repository.MovieJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepository {

    private final MovieJpaRepository movieJpaRepository;
    private final GenreJpaRepository genreJpaRepository;
    private final MovieMapper mapper;

    @Override
    public Movie create(Movie movie, Set<Long> genreIds) {
        MovieJpaEntity entity = mapper.toEntity(movie);

        // Gán genres bằng managed references (proxy)
        if (genreIds != null && !genreIds.isEmpty()) {
            Set<GenreJpaEntity> genres = genreIds.stream()
                    .map(genreJpaRepository::getReferenceById)
                    .collect(Collectors.toSet());
            entity.setGenres(genres);
        }

        // Lưu entity
        MovieJpaEntity saved = movieJpaRepository.save(entity);
        // Flush để đảm bảo dữ liệu được ghi xuống DB và session vẫn còn mở
        movieJpaRepository.flush();

        // Lấy lại entity với genres đã được fetch đầy đủ (tránh LazyInitializationException)
        MovieJpaEntity loaded = movieJpaRepository.findWithGenresById(saved.getId())
                .orElseThrow(() -> new IllegalStateException("Failed to load movie after save"));

        return mapper.toDomain(loaded);
    }

    @Override
    public Movie update(Movie movie, Set<Long> genreIds) {
        MovieJpaEntity entity = movieJpaRepository.findByIdOrThrow(movie.getId().getValue());

        mapper.updateEntity(entity, movie);

        // Cập nhật genres
        if (genreIds != null) {
            Set<GenreJpaEntity> genres = genreIds.stream()
                    .map(genreJpaRepository::getReferenceById)
                    .collect(Collectors.toSet());
            entity.setGenres(genres);
        }

        MovieJpaEntity saved = movieJpaRepository.save(entity);
        movieJpaRepository.flush();

        MovieJpaEntity loaded = movieJpaRepository.findWithGenresById(saved.getId())
                .orElseThrow(() -> new IllegalStateException("Failed to load movie after update"));

        return mapper.toDomain(loaded);
    }

    @Override
    public Optional<Movie> findById(MovieId id) {
        // Sử dụng findWithGenresById để luôn có genres khi cần
        return movieJpaRepository.findWithGenresById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public Page<Movie> findAll(Pageable pageable) {
        // Phân trang không fetch genres (có thể gây LazyInitializationException nếu truy cập genres ngoài transaction)
        // Nếu cần genres cho list, có thể cấu hình EntityGraph cho phương thức findAll hoặc dùng DTO projection.
        // Hiện tại giữ nguyên, khi map toDomain có thể bị lỗi nếu truy cập genres.
        // Để an toàn, bạn có thể dùng fetch join trong query riêng.
        return movieJpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(MovieId id) {
        MovieJpaEntity entity = movieJpaRepository.findByIdOrThrow(id.getValue());
        movieJpaRepository.delete(entity);
    }

    @Override
    public boolean existsById(MovieId id) {
        return movieJpaRepository.existsById(id.getValue());
    }

    @Override
    public boolean existsByTitle(String title) {
        return movieJpaRepository.existsByTitle(title);
    }

    @Override
    public boolean existsByTitleAndIdNot(String title, MovieId id) {
        return movieJpaRepository.existsByTitleAndIdNot(title, id.getValue());
    }
}