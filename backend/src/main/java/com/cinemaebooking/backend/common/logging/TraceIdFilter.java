package com.cinemaebooking.backend.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * TraceIdFilter - Inject traceId into MDC for each request.
 * Responsibility:
 * - Ensure every request has a unique traceId
 * - Propagate traceId across logs and response
 * - Support traceId from external systems (gateway, etc.)
 */
@Slf4j
@Component
public class TraceIdFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_KEY = "traceId";
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String traceId = resolveTraceId(request);

        try {
            MDC.put(TRACE_ID_KEY, traceId);
            response.setHeader(TRACE_ID_HEADER, traceId);

            log.info("[{}] {} {}", traceId,
                    request.getMethod(),
                    request.getRequestURI());

            filterChain.doFilter(request, response);

        } finally {
            MDC.clear();
        }
    }

    private String resolveTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(TRACE_ID_HEADER);
        return (traceId != null && !traceId.isBlank())
                ? traceId.trim()
                : UUID.randomUUID().toString();
    }
}