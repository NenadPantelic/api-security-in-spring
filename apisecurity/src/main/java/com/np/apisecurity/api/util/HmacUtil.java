package com.np.apisecurity.api.util;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacUtil {

    private static final String HMAC_SHA256 = "HmacSHA256";

    public static String computeHmac(String message, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_SHA256);
        var secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        mac.init(secretKeySpec);

        var hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(hmacBytes));
    }

    public static boolean doHmacMatch(String message, String secretKey, String hmacValue) throws NoSuchAlgorithmException, InvalidKeyException {
        var computedHmacValue = computeHmac(message, secretKey);
        return StringUtils.equals(computedHmacValue, hmacValue);
    }
}
