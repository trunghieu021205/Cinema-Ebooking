package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.combo.domain.valueobject.ComboId;
import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ComboExceptions {

    public static BaseException notFound(ComboId id) {
        return new BaseException(ErrorCode.COMBO_NOT_FOUND,
                "Combo not found with id: " + id);
    }

    public static BaseException alreadyExists(String name) {
        return new BaseException(ErrorCode.COMBO_ALREADY_EXISTS,
                "Combo already exists with name: " + name);
    }

    public static BaseException invalidStatus() {
        return new BaseException(ErrorCode.COMBO_INVALID_STATUS,
                "Invalid combo status");
    }

    public static BaseException outOfStock() {
        return new BaseException(ErrorCode.COMBO_OUT_OF_STOCK,
                "Combo is out of stock");
    }
}