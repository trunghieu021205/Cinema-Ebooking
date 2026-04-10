package com.cinemaebooking.backend.movie.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.movie.domain.enums.AgeRating;
import com.cinemaebooking.backend.movie.domain.enums.MovieStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

/**
 * MovieJpaEntity: Mapping JPA cho Movie domain entity.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Mapping bảng "movies"</li>
 *     <li>Lưu thông tin chi tiết phim</li>
 *     <li>Liên kết với Genre</li>
 *     <li>Kế thừa auditing + soft delete</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Genre là Many-to-Many</li>
 *     <li>Director/Actors đang lưu dạng text (MVP)</li>
 *     <li>Rating là giá trị trung bình + số lượt đánh giá</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class MovieJpaEntity extends BaseJpaEntity {

    /**
     * Tên phim
     */
    @Column(nullable = false, length = 255)
    private String title;

    /**
     * Mô tả nội dung phim
     */
    @Column(length = 1000)
    private String description;

    /**
     * Thời lượng (phút)
     */
    @Column(nullable = false)
    private Integer duration;

    /**
     * Phân loại độ tuổi
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AgeRating ageRating;

    /**
     * Ngày khởi chiếu
     */
    @Column(nullable = false)
    private LocalDate releaseDate;

    /**
     * Trạng thái phim
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MovieStatus status;

    /**
     * Poster phim
     */
    @Column(length = 500)
    private String posterUrl;

    /**
     * Banner phim
     */
    @Column(length = 500)
    private String bannerUrl;

    /**
     * Đạo diễn (MVP: dạng text)
     */
    @Column(length = 255)
    private String director;

    /**
     * Danh sách diễn viên (MVP: dạng text)
     */
    @Column(length = 500)
    private String actors;

    /**
     * Thể loại phim
     */
    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<GenreJpaEntity> genres;

    /**
     * Điểm đánh giá trung bình
     */
    private Double rating;// cache

    /**
     * Số lượt đánh giá
     */
    private Integer ratingCount;// cache
}