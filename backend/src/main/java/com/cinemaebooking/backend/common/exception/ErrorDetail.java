package com.cinemaebooking.backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ErrorDetail {
    private String field;
    private ErrorCategory category;
    private String reason;
    private Map<String, Object> params;  // ← thêm

    // ✅ Giữ constructor cũ để các rule chưa cần params không phải sửa
    public ErrorDetail(String field, ErrorCategory category, String reason) {
        this(field, category, reason, null);
    }
}
