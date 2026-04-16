package com.cinemaebooking.backend.common.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

/**
 * BaseEntity - Root class for all Domain Entities.
 * Core Rule:
 * - Represents domain identity only
 * - Must not contain any persistence, JPA, or framework-specific logic
 * - Equality is based solely on domain identity (id)
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@SuperBuilder(toBuilder = true)
public abstract class BaseEntity<ID extends BaseId> {

    protected ID id;

    /**
     * Two entities are considered the same if they have the same domain identity.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity<?> other = (BaseEntity<?>) o;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id);
    }
}