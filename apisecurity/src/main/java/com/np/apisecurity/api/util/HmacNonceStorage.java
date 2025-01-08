package com.np.apisecurity.api.util;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// dummy, just to serve these needs
public class HmacNonceStorage {

    private static final Map<String, LocalDateTime> NONCE_MAP = new HashMap<>();

    // two requests having the same HMAC within  5 minutes will be rejected
    public static boolean addWhenNotExists(String nonce) {
        if (NONCE_MAP.containsKey(nonce)) {
            return false;
        }

        NONCE_MAP.put(nonce, LocalDateTime.now());
        return true;
    }

    // we need a scheduler to evict nonces which are expired; on production grade, if we use Redis, we can set expiry
    // time
    @Scheduled(fixedDelay = 2000) // every 2s
    private static void cleanExpiredNonces() {
        var nonceTimeLimit = LocalDateTime.now().minusMinutes(5);
        var expiredKeys = NONCE_MAP.keySet().stream()
                .filter(k -> NONCE_MAP.get(k).isBefore(nonceTimeLimit))
                .toList();
        expiredKeys.forEach(NONCE_MAP::remove);
    }

}
