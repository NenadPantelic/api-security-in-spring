package com.np.apisecurity.api.server.encodedecode;

import com.np.apisecurity.api.util.HmacUtil;
import com.np.apisecurity.dto.request.HmacData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/api/v1/hmac")
public class HmacApi {

    public static final String SECRET_KEY = "The123HmacSecretKey";
    private static final String MESSAGE_DELIMITER = "\n";

    @PostMapping(value = "/calculate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String hmac(@RequestHeader("X-Verb-Calculate") String httpVerb,
                       @RequestHeader("X-Uri-Calculate") String uri,
                       @RequestHeader("X-Register-Date") String registerDate,
                       @RequestHeader("X-Nonce") String nonce,
                       @RequestBody HmacData hmacData) throws NoSuchAlgorithmException, InvalidKeyException {
        var hmacMessage = constructHmacMessage(httpVerb, uri, registerDate, nonce, hmacData);
        return HmacUtil.computeHmac(hmacMessage, SECRET_KEY);
    }

    @PostMapping(value = "/info", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String info(@RequestBody HmacData hmacData) {
        return String.format("The request body: %s", hmacData);
    }

    public static String constructHmacMessage(String httpVerb,
                                              String uri,
                                              String registerDate,
                                              String nonce,
                                              HmacData hmacData) {
        return String.format(
                "%s%s%s%s%s%s%s%s%s%s%s%s",
                httpVerb, MESSAGE_DELIMITER,
                uri, MESSAGE_DELIMITER,
                hmacData.amount(), MESSAGE_DELIMITER,
                hmacData.fullName(), MESSAGE_DELIMITER,
                registerDate, MESSAGE_DELIMITER,
                nonce, MESSAGE_DELIMITER
        );
    }
}
