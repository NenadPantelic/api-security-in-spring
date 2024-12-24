package com.np.apisecurity.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.np.apisecurity.dto.internal.JweData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.util.Date;
import java.util.Optional;

@Service
public class JweService {

    private static final String CONFIDENTIAL_DATA = "confidentialData001Sample";
    private static final String ENCRYPTION_KEY = "TheMandatory32BytesEncryptionKey";

    private static final String DUMMY_ATTRIBUTE_KEY = "dummyAttribute";
    private static final String CONFIDENTIAL_DATA_KEY = "confidentialData";


    public String store(JweData jweData) {
        try {
            var expiresAt = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15);
            var jweClaims = new JWTClaimsSet.Builder()
                    .expirationTime(Date.from(expiresAt.toInstant(ZoneOffset.UTC)))
                    .subject(jweData.getUsername())
                    .claim(DUMMY_ATTRIBUTE_KEY, jweData.getDummyAttribute())
                    .claim(CONFIDENTIAL_DATA_KEY, CONFIDENTIAL_DATA)
                    .build();

            // JWE header with 256 bits encryption
            var jweHeader = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM);
            var jwe = new EncryptedJWT(jweHeader, jweClaims);
            DirectEncrypter encrypter = null;

            encrypter = new DirectEncrypter(ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8));
            jwe.encrypt(encrypter);
            return jwe.serialize();
        } catch (JOSEException e) {
            return StringUtils.EMPTY;
        }
    }

    public Optional<JweData> read(String jweToken) {
        try {
            var jwe = EncryptedJWT.parse(jweToken);
            var decrypter = new DirectDecrypter(ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8));
            jwe.decrypt(decrypter);

            var jweClaims = jwe.getJWTClaimsSet();
            var now = new Date();
            var confidentialData = jweClaims.getStringClaim(CONFIDENTIAL_DATA_KEY);

            if (now.before(jweClaims.getExpirationTime()) && StringUtils.equals(confidentialData, CONFIDENTIAL_DATA)) {
                var jweData = new JweData(jweClaims.getSubject(), jweClaims.getStringClaim(DUMMY_ATTRIBUTE_KEY));
                return Optional.of(jweData);
            }

            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
