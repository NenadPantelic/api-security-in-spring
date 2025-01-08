package com.np.apisecurity.api.server.encodedecode;

import com.np.apisecurity.api.util.EncryptDecryptUtil;
import com.np.apisecurity.dto.request.DataToEncodeDecode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class EncryptDecryptApi {

    // 128, 192 or 256 bits
    // 16, 24 or 32 chars
    private static final String SECRET_KEY = "MySecretKey12345";


    @PostMapping(value = "/encrypt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String encryptAes(@RequestBody DataToEncodeDecode dataToEncodeDecode) throws Exception {
        return EncryptDecryptUtil.encryptAes(dataToEncodeDecode.text(), SECRET_KEY);
    }

    @PostMapping(value = "/decrypt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String decryptAes(@RequestBody DataToEncodeDecode dataToEncodeDecode) throws Exception {
        return EncryptDecryptUtil.decryptAes(dataToEncodeDecode.text(), SECRET_KEY);
    }
}
