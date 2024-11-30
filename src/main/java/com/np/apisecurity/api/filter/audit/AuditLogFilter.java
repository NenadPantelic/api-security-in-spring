package com.np.apisecurity.api.filter.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.apisecurity.api.filter.CachedBodyHttpServletRequest;
import com.np.apisecurity.dto.internal.AuditLogEntry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class AuditLogFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public AuditLogFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var cachedRequest = new CachedBodyHttpServletRequest(request);
        var requestBody = IOUtils.toString(cachedRequest.getReader());

        var auditLogEntry = new AuditLogEntry(
                LocalDateTime.now().toString(),
                request.getRequestURI(),
                request.getQueryString(),
                request.getMethod(),
                requestBody,
                request.getHeader(HttpHeaders.AUTHORIZATION),
                response.getStatus()
        );
        var logString = objectMapper.writeValueAsString(auditLogEntry);
        log.info(logString);
    }
}
