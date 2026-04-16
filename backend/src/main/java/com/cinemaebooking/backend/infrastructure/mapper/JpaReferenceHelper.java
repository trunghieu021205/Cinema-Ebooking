package com.cinemaebooking.backend.infrastructure.mapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * JpaReferenceHelper - Lightweight JPA reference resolver.
 *
 * PURPOSE:
 * - Avoid unnecessary SELECT queries
 * - Provide lazy proxy references for relationships
 *
 * WARNING:
 * - DO NOT use for business logic
 * - DO NOT access fields outside persistence mapping
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public abstract class JpaReferenceHelper {

    @PersistenceContext
    protected EntityManager entityManager;

    protected <T> T ref(Class<T> clazz, Long id) {
        if (id == null) return null;
        return entityManager.getReference(clazz, id);
    }
}