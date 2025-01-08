package com.np.apisecurity.entity.apikey;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class BasicApiKey {

    @Id
    private int apiKeyId;
    private int userId;
    private String apiKey;
    private LocalDateTime expiresAt;
}
