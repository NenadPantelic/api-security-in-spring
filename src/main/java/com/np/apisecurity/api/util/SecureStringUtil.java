package com.np.apisecurity.api.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecureStringUtil {

    private static final String STRING_SEED = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom SECURE_RANDOM;

    static {
        try {
            SECURE_RANDOM = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateRandomString(int length) {
        var sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int secureRandomIndex = SECURE_RANDOM.nextInt(STRING_SEED.length());
            sb.append(STRING_SEED.charAt(secureRandomIndex));
        }

        return sb.toString();
    }

    public static boolean equals(String first, String second) {
        return MessageDigest.isEqual(first.getBytes(StandardCharsets.UTF_8), second.getBytes(StandardCharsets.UTF_8));
    }

}
