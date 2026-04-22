package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenreExceptions {

    public static BaseException notFound(GenreId id) {
        // Sử dụng mã lỗi mới cần được thêm vào ErrorCode (GENRE_NOT_FOUND = 3008)
        // Tạm thời dùng RESOURCE_NOT_FOUND, nhưng thực tế phải xin phép lead
        return new BaseException(ErrorCode.GENRE_NOT_FOUND,
                "Genre not found with id: " + id);
    }

    public static BaseException duplicateName(String name) {
        // Sử dụng mã lỗi mới cần được thêm vào ErrorCode (GENRE_ALREADY_EXISTS = 3009)
        return new BaseException(ErrorCode.GENRE_ALREADY_EXISTS,
                "Genre already exists with name: " + name);
    }
}