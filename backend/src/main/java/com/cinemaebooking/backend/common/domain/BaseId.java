package com.cinemaebooking.backend.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * BaseId - Type-safe identifier for Domain Entities.
 * Core Purpose:
 * - Prevent mixing IDs between different domains
 * - Provide type-safe identity in Domain Layer
 * - Keep independence from persistence layer
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@RequiredArgsConstructor
public abstract class BaseId {

    /**
     * Raw identifier value.
     */
    private final Long value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId other = (BaseId) o;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), value);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + value + ")";
    }
}