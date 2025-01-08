package com.np.apisecurity.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.np.apisecurity.dto.internal.JwtData;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class JwtService {

    private static final String HMAC_SECRET = "theHmacSecretKeyForJwt";
    private static final String ISSUER = "apisecurity.com";
    private static final String[] AUDIENCE = {"https://apisecurity.com", "https://www.apisecurity.com"};
    private static final String SAMPLE_PRIVATE_CLAIM = "just-some-private-claim-to-be-validated";
    private static final String DUMMY_ATTRIBUTE_CLAIM_KEY = "dummyAttribute";
    private static final String SAMPLE_PRIVATE_CLAIM_KEY = "samplePrivateClaim";


    public String store(JwtData jwtData) {
        var algorithm = Algorithm.HMAC256(HMAC_SECRET);
        var expiresAt = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15);

        return JWT.create()
                .withSubject(jwtData.getUsername())
                .withIssuer(ISSUER)
                .withAudience(AUDIENCE)
                .withExpiresAt(Date.from(expiresAt.toInstant(ZoneOffset.UTC)))
                .withClaim(DUMMY_ATTRIBUTE_CLAIM_KEY, jwtData.getDummyAttribute())
                .withClaim(SAMPLE_PRIVATE_CLAIM_KEY, SAMPLE_PRIVATE_CLAIM)
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

            var jwtData = new JwtData(
                    decodedJwtToken.getSubject(),
                    decodedJwtToken.getClaim(DUMMY_ATTRIBUTE_CLAIM_KEY).asString()
            );
            return Optional.of(jwtData);
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }

}
