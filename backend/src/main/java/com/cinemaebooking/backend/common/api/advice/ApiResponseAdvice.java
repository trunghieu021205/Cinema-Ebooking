package com.cinemaebooking.backend.common.api.advice;

import com.cinemaebooking.backend.common.api.response.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType contentType,
            Class<? extends HttpMessageConverter<?>> converterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {

        if (body == null) return null;

        if (body instanceof ApiResponse<?>) {
            return body;
        }

        return ApiResponse.<Object>builder()
                .data(body)
                .timestamp(Instant.now())
                .traceId(resolveTraceId(request))
                .build();
    }

    private String resolveTraceId(ServerHttpRequest request) {
        String traceId = request.getHeaders().getFirst("X-Trace-Id");
        return (traceId != null && !traceId.isBlank())
                ? traceId
                : "no-trace-id";
    }
}