package com.cinemaebooking.backend.common.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * BaseEntity: Lớp cơ sở cho tất cả Domain Entity.
 *
 * Nguyên tắc:
 * <ul>
 *     <li>Không chứa annotation JPA</li>
 *     <li>Không chứa logic liên quan database (audit, soft delete,...)</li>
 *     <li>Chỉ chứa identity và business logic</li>
 * </ul>
 *
 * @param <ID> kiểu ID của entity (kế thừa BaseId)
 */
@Getter
@SuperBuilder(toBuilder = true)
public abstract class BaseEntity<ID extends BaseId> {

    /**
     * ID của entity trong domain.
     * Đây là identity chính trong business logic.
     */
    protected ID id;

    /**
     * So sánh entity theo ID (identity equality)
     */
    public boolean sameIdentityAs(BaseEntity<ID> other) {
        return other != null && this.id.equals(other.id);
    }
}