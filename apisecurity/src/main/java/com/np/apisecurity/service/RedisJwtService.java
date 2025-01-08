package com.np.apisecurity.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.np.apisecurity.api.util.SecureStringUtil;
import com.np.apisecurity.dto.internal.JwtData;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class RedisJwtService {

    private static final String HMAC_SECRET = "theHmacSecretKeyForJwt";
    private static final String ISSUER = "apisecurity.com";
    private static final String[] AUDIENCE = {"https://apisecurity.com", "https://www.apisecurity.com"};
    private static final String SAMPLE_PRIVATE_CLAIM = "just-some-private-claim-to-be-validated";
    private static final String DUMMY_ATTRIBUTE_CLAIM_KEY = "dummyAttribute";
    private static final String SAMPLE_PRIVATE_CLAIM_KEY = "samplePrivateClaim";

    private final StatefulRedisConnection<String, String> redisConnection;

    public RedisJwtService(StatefulRedisConnection<String, String> redisConnection) {
        this.redisConnection = redisConnection;
    }

    public String store(JwtData jwtData) {
        var algorithm = Algorithm.HMAC256(HMAC_SECRET);
        var expiresAt = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15);
        var expiresAtDate = Date.from(expiresAt.toInstant(ZoneOffset.UTC));

        var jwtId = SecureStringUtil.generateRandomString(30);
        redisConnection.sync().set(jwtId, StringUtils.EMPTY);
        redisConnection.sync().expireat(jwtId, expiresAtDate);

        return JWT.create()
                .withSubject(jwtData.getUsername())
                .withIssuer(ISSUER)
                .withAudience(AUDIENCE)
                .withExpiresAt(expiresAtDate)
                .withClaim(DUMMY_ATTRIBUTE_CLAIM_KEY, jwtData.getDummyAttribute())
                .withClaim(SAMPLE_PRIVATE_CLAIM_KEY, SAMPLE_PRIVATE_CLAIM)
                .withJWTId(jwtId)
                .sign(algorithm);
    }

    public Optional<JwtData> read(String jwtToken) {
        try {
            var algorithm = Algorithm.HMAC256(HMAC_SECRET);
            var jwtVerifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .withAnyOfAudience(AUDIENCE)
                    .acceptExpiresAt(60)
                    .withClaim(SAMPLE_PRIVATE_CLAIM_KEY, SAMPLE_PRIVATE_CLAIM)
                    // dummy attribute is dynamic data, we will not verify it
                    .build();
            var decodedJwtToken = jwtVerifier.verify(jwtToken);

            var jwtId = decodedJwtToken.getId();
            var jwtIdInRedis = redisConnection.sync().get(jwtId);
            if (StringUtils.isBlank(jwtIdInRedis)) {
                return Optional.empty();
            }

            var jwtData = new JwtData(
                    decodedJwtToken.getSubject(),
                    decodedJwtToken.getClaim(DUMMY_ATTRIBUTE_CLAIM_KEY).asString()
            );
            return Optional.of(jwtData);
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }


    public void delete(String jwtToken) {
        var algorithm = Algorithm.HMAC256(HMAC_SECRET);
        var jwtVerifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .withAnyOfAudience(AUDIENCE)
                .acceptExpiresAt(60)
                .withClaim(SAMPLE_PRIVATE_CLAIM_KEY, SAMPLE_PRIVATE_CLAIM)
                .build();
        var decodedJwtToken = jwtVerifier.verify(jwtToken);

        redisConnection.sync().del(decodedJwtToken.getId());
    }
}
