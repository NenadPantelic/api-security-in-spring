package com.np.apisecurity.api.server.encodedecode;

import com.np.apisecurity.api.util.EncodeDecodeUtil;
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
public class EncodeDecodeApi {

    @PostMapping(value = "/encode/base64", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String encodeBase64(@RequestBody DataToEncodeDecode dataToEncodeDecode) {
        return EncodeDecodeUtil.encodeBase64(dataToEncodeDecode.text());
    }

    @PostMapping(value = "/decode/base64", produces = MediaType.TEXT_PLAIN_VALUE)
    public String decodeBase64(@RequestBody DataToEncodeDecode dataToEncodeDecode) {
        return EncodeDecodeUtil.decodeBase64(dataToEncodeDecode.text());
    }

    @PostMapping(value = "/encode/url", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String encodeUrl(@RequestBody DataToEncodeDecode dataToEncodeDecode) {
        return EncodeDecodeUtil.encodeUrl(dataToEncodeDecode.text());
    }

    @PostMapping(value = "/decode/url", produces = MediaType.TEXT_PLAIN_VALUE)
    public String decodeUrl(@RequestBody DataToEncodeDecode dataToEncodeDecode) {
        return EncodeDecodeUtil.decodeUrl(dataToEncodeDecode.text());
    }
}
