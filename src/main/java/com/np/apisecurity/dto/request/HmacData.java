package com.np.apisecurity.dto.request;

public record HmacData(String fullName,
                       String city,
                       String gender,
                       int amount) {
}
