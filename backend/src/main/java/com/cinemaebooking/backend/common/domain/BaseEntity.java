package com.cinemaebooking.backend.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * BaseEntity: Tất cả entity trong domain layer đều kế thừa từ lớp này.
 * <p>
 * Hỗ trợ:
 * <ul>
 *     <li>Soft Delete: deletedAt, deletedBy</li>
 *     <li>Auditing: createdAt, createdBy, updatedAt, updatedBy</li>
 *     <li>Helper methods để markCreated, markUpdated, markAsDeleted, restore</li>
 * </ul>
 *
 * @param <ID> kiểu của ID entity (thường kế thừa BaseId)
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public abstract class BaseEntity<ID extends BaseId> {

    /**
     * ID của entity trong domain layer.
     */
    private ID id;

    // ====================== AUDITING FIELDS ======================

    /**
     * Thời điểm entity được tạo (tự động cập nhật khi markCreated).
     */
    private LocalDateTime createdAt;

    /**
     * Thời điểm entity được cập nhật lần cuối (tự động cập nhật khi markCreated hoặc markUpdated).
     */
    private LocalDateTime updatedAt;

    /**
     * Người tạo entity (username hoặc userId).
     */
    private String createdBy;

    /**
     * Người cập nhật entity lần cuối (username hoặc userId).
     */
    private String updatedBy;

    // ====================== SOFT DELETE ======================

    /**
     * Thời điểm entity bị soft delete. Null nếu chưa bị xóa.
     */
    private LocalDateTime deletedAt;

    /**
     * Người thực hiện soft delete. Null nếu chưa bị xóa.
     */
    private String deletedBy;

    // ====================== HELPER METHODS ======================

    /**
     * Kiểm tra entity đã bị xóa mềm hay chưa.
     *
     * @return true nếu deletedAt != null, false nếu chưa xóa
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * Đánh dấu entity đã bị soft delete với thông tin người thực hiện.
     * Đồng thời cập nhật updatedAt.
     *
     * @param deletedBy username hoặc userId thực hiện xóa
     */
    public void markAsDeleted(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Khôi phục entity đã bị soft delete.
     * Xóa deletedAt và deletedBy, đồng thời cập nhật updatedAt.
     */
    public void restore() {
        this.deletedAt = null;
        this.deletedBy = null;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Đánh dấu entity mới được tạo, thiết lập auditing ban đầu.
     * Gán createdAt, updatedAt, createdBy, updatedBy.
     *
     * @param createdBy username hoặc userId tạo entity
     */
    public void markCreated(String createdBy) {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
    }

    /**
     * Cập nhật auditing khi entity được chỉnh sửa.
     * Chỉ cập nhật updatedAt và updatedBy.
     *
     * @param updatedBy username hoặc userId thực hiện update
     */
    public void markUpdated(String updatedBy) {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = updatedBy;
    }
}