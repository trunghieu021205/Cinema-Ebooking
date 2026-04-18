package com.cinemaebooking.backend.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class SoftDeleteJpaRepositoryImpl<T extends BaseJpaEntity>
        extends SimpleJpaRepository<T, Long>
        implements SoftDeleteJpaRepository<T> {

    public SoftDeleteJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
                                       EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    // ================== SOFT DELETE OVERRIDE ==================
    @Override
    public void delete(T entity) {
        entity.softDelete();
        save(entity);
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void softDeleteById(Long id) {
        deleteById(id);
    }

    // Optional: Lấy danh sách đã soft delete (cho admin/audit)
    public List<T> findAllDeleted() {
        return findAll((root, cq, cb) -> cb.isNotNull(root.get("deletedAt")));
    }
}