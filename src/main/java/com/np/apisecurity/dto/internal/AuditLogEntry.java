package com.np.apisecurity.dto.internal;

public record AuditLogEntry(String timestamp,
                            String method,
                            String path,
                            String query,
                            String headers,
                            String requestBody,
                            int responseStatusCode) {
}
