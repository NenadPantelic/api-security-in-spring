package com.np.apisecurity.api.server.auth.basic;

import com.np.apisecurity.api.util.EncryptDecryptUtil;
import com.np.apisecurity.api.util.HashUtil;
import com.np.apisecurity.api.util.SecureStringUtil;
import com.np.apisecurity.dto.request.NewBasicAuthUser;
import com.np.apisecurity.entity.basicauth.BasicAuthUser;
import com.np.apisecurity.repository.basicauth.BasicAuthUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/auth/basic/v1")
public class BasicAuthApi {

    public static final String SECRET_KEY = "TheSecretKey2468";

    private final BasicAuthUserRepository basicAuthUserRepository;


    public BasicAuthApi(BasicAuthUserRepository basicAuthUserRepository) {
        this.basicAuthUserRepository = basicAuthUserRepository;
    }

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTime() {
        return "Now is " + LocalDateTime.now();
    }

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createUser(@RequestBody NewBasicAuthUser newBasicAuthUser) throws
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        var encryptedUsername = EncryptDecryptUtil.encryptAes(newBasicAuthUser.username(), SECRET_KEY);
        var salt = SecureStringUtil.generateRandomString(16);
        var passwordHash = HashUtil.bcrypt(newBasicAuthUser.password(), salt);

        var user = BasicAuthUser.builder()
                .displayName(newBasicAuthUser.displayName())
                .salt(salt)
                .passwordHash(passwordHash)
                .username(encryptedUsername)
                .build();
        user = basicAuthUserRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("New user created: " + user.getDisplayName());
    }

}
