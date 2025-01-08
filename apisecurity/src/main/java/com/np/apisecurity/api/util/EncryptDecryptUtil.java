package com.np.apisecurity.api.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class EncryptDecryptUtil {

    private static final String ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS7Padding";
    // AES requires an IV - initialization vector
    // its basic requirement is uniqueness - no IV may be reused under the same key
    // it should be stored securely (in Hashicorp Vault e.g.)
    // in this example, it is hardcoded in code (bad practise)
    private static final String IV = "LqV4g8gT1DqjSPB6"; // 16-byte (char) string

    // the attacker must know the secret key and IV
    private static IvParameterSpec paramSpec;
    private static Cipher cipher;

    static {
        Security.addProvider(new BouncyCastleProvider());
        paramSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        try {
            cipher = Cipher.getInstance(AES_TRANSFORMATION);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptAes(String original, String secretKey) throws IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        final var secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, paramSpec);

        // AES supports Base64 encode or hex encode
        var encryptedBytes = cipher.doFinal(original.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(encryptedBytes));
    }

    public static String decryptAes(String encrypted, String secretKey) throws InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        final var secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, paramSpec);

        var encryptedBytes = Hex.decode(encrypted);
        var decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

}
