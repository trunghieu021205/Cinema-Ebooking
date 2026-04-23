package com.cinemaebooking.backend.combo.domain.model;

import com.cinemaebooking.backend.combo.domain.enums.ComboStatus;
import com.cinemaebooking.backend.combo.domain.valueobject.ComboId;
import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@SuperBuilder
public class Combo extends BaseEntity<ComboId> {

    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String imageUrl;
    private ComboStatus status;

    // ================== BUSINESS METHODS ==================

    public void update(String name,
                       String description,
                       BigDecimal price,
                       BigDecimal originalPrice,
                       String imageUrl,
                       ComboStatus status) {
        validateName(name);
        validatePrice(price);
        validateStatus(status);
        validateOptionalFields(originalPrice);

        this.name = name.trim();
        this.description = description != null ? description.trim() : null;
        this.price = price;
        this.originalPrice = originalPrice;
        this.imageUrl = imageUrl != null ? imageUrl.trim() : null;
        this.status = status;
    }

    public void changeStatus(ComboStatus status) {
        validateStatus(status);
        this.status = status;
    }

    // ================== VALIDATION ==================

    public void validateForCreate() {
        validateName(this.name);
        validatePrice(this.price);
        validateStatus(this.status != null ? this.status : ComboStatus.ACTIVE);
        validateOptionalFields(this.originalPrice);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw CommonExceptions.invalidInput("Combo name must not be empty");
        }
        if (name.trim().length() < 3 || name.trim().length() > 150) {
            throw CommonExceptions.invalidInput("Combo name length must be between 3 and 150");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw CommonExceptions.invalidInput("Combo price must not be null");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw CommonExceptions.invalidInput("Combo price must be greater than 0");
        }
    }

    private void validateStatus(ComboStatus status) {
        if (status == null) {
            throw CommonExceptions.invalidInput("Combo status must not be null");
        }
    }

    private void validateOptionalFields(BigDecimal originalPrice) {
        if (originalPrice != null && originalPrice.compareTo(this.price) < 0) {
            throw CommonExceptions.invalidInput("Original price cannot be less than current price");
        }
    }
}