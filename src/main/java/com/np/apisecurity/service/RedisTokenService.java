package com.np.apisecurity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.apisecurity.api.util.HmacUtil;
import com.np.apisecurity.api.util.SecureStringUtil;
import com.np.apisecurity.dto.internal.RedisToken;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Optional;

@Service
public class RedisTokenService {

    private static final String HMAC_SECRET = "theHmacSecretKey";

    private final StatefulRedisConnection<String, String> redisConnection;
    private final ObjectMapper objectMapper;

    public RedisTokenService(StatefulRedisConnection<String, String> redisConnection, ObjectMapper objectMapper) {
        this.redisConnection = redisConnection;
        this.objectMapper = objectMapper;
    }

    public String store(RedisToken token) {
        var tokenId = SecureStringUtil.generateRandomString(30);
        try {
            String tokenJson = objectMapper.writeValueAsString(token);
            redisConnection.sync().set(tokenId, tokenJson);
            redisConnection.sync().expire(tokenId, Duration.ofMinutes(15));

            var hmac = HmacUtil.computeHmac(tokenId, HMAC_SECRET);
            return String.format("%s.%s", tokenId, hmac);
        } catch (JsonProcessingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    public Optional<RedisToken> read(String bearerToken) {
        var tokens = StringUtils.split(bearerToken, ".");
        if (tokens.length != 2) {
            return Optional.empty();
        }

        var tokenId = tokens[0];
        var hmac = tokens[1];

        try {
            if (!HmacUtil.doHmacMatch(tokenId, HMAC_SECRET, hmac)) {
                return Optional.empty();
            }

            var tokenJson = redisConnection.sync().get(tokenId);
            if (StringUtils.isBlank(tokenJson)) {
                return Optional.empty();
            }

            RedisToken redisToken = objectMapper.readValue(tokenJson, RedisToken.class);
            return Optional.of(redisToken);
        } catch (JsonProcessingException | NoSuchAlgorithmException |
                 InvalidKeyException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void delete(String tokenId) {
        redisConnection.sync().del(tokenId);
    }
}
