package com.np.apisecurity.dto.request;

public record NewBasicAuthUser(String username,
                               String password,
                               String displayName) {
}
