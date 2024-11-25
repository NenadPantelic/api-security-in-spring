package com.np.apisecurity.api.util;


import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncodeDecodeUtil {

    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    public static String encodeBase64(String original) {
        return ENCODER.encodeToString(original.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeBase64(String encoded) {
        return new String(DECODER.decode(encoded.getBytes()));
    }

    public static String encodeUrl(String original) {
        return UriUtils.encode(original, StandardCharsets.UTF_8);
    }

    public static String decodeUrl(String encoded) {
        return UriUtils.decode(encoded, StandardCharsets.UTF_8);
    }
}
