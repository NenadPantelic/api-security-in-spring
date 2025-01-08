package com.np.apisecurity.api.util;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.generators.OpenBSDBCrypt;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String sha256(String original, String salt) throws NoSuchAlgorithmException {
        // salt can be added anywhere, we are inserting it at the beginning just for simplicity
        var originalWithSalt = salt + original;
        var digest = MessageDigest.getInstance("SHA-256");
        var hash = digest.digest(originalWithSalt.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(hash));
    }

    public static boolean isSha256Match(String original, String salt, String hashValue) throws NoSuchAlgorithmException {
        var newHashValue = sha256(original, salt);
        return StringUtils.equals(newHashValue, hashValue);
    }

    public static String bcrypt(String original, String salt) {
        return OpenBSDBCrypt.generate(
                original.getBytes(StandardCharsets.UTF_8),
                salt.getBytes(StandardCharsets.UTF_8),
                5
        );
    }

    // no need to pass the salt, as salt is already present in hash value
    public static boolean isBcryptMatch(String original, String hashValue) {
        return OpenBSDBCrypt.checkPassword(hashValue, original.getBytes(StandardCharsets.UTF_8));
    }
}
