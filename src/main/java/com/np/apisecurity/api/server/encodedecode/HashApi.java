package com.np.apisecurity.api.server.encodedecode;

import com.np.apisecurity.api.util.HashUtil;
import com.np.apisecurity.api.util.SecureStringUtil;
import com.np.apisecurity.dto.request.DataToEncodeDecode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/hash")
public class HashApi {

    private static final int SALT_LENGTH = 16;
    // can be stored in the database as well
    // every time the same string is hashed, a different value is computed because the salt is different
    private final Map<String, String> saltMap = new HashMap<>();


    @PostMapping(value = "/sha256", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String sha256(@RequestBody DataToEncodeDecode dataToEncodeDecode) throws NoSuchAlgorithmException {
        var salt = SecureStringUtil.generateRandomString(SALT_LENGTH);
        var text = dataToEncodeDecode.text();
        saltMap.put(text, salt);
        return HashUtil.sha256(text, salt);
    }

    @PostMapping(value = "/sha256/match", produces = MediaType.TEXT_PLAIN_VALUE)
    public String sha256Match(@RequestHeader(name = "X-Hash") String hashValue,
                              @RequestBody DataToEncodeDecode dataToEncodeDecode) throws NoSuchAlgorithmException {
        var text = dataToEncodeDecode.text();
        var salt = Optional.ofNullable(saltMap.get(text)).orElse(StringUtils.EMPTY);
        return HashUtil.isSha256Match(text, salt, hashValue) ? "MATCH" : "NOT MATCH";
    }

    @PostMapping(value = "/bcrypt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String bcrypt(@RequestBody DataToEncodeDecode dataToEncodeDecode) {
        var salt = SecureStringUtil.generateRandomString(SALT_LENGTH);
        return HashUtil.bcrypt(dataToEncodeDecode.text(), salt);
    }

    @PostMapping(value = "/bcrypt/match", produces = MediaType.TEXT_PLAIN_VALUE)
    public String bcryptMatch(@RequestHeader(name = "X-Hash") String hashValue,
                              @RequestBody DataToEncodeDecode dataToEncodeDecode) {
        return HashUtil.isBcryptMatch(dataToEncodeDecode.text(), hashValue) ? "MATCH" : "NOT MATCH";
    }
}
