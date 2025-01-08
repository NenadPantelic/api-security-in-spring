package com.np.apisecurity.api.server.auth.apikey;

import com.np.apisecurity.constant.ApiKeyConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth/api-key/v1")
@RestController
public class BasicApiKeyApi {

    @GetMapping(value = "/add", produces = MediaType.TEXT_PLAIN_VALUE)
    public String add(@RequestParam("a") int a, @RequestParam("b") int b, HttpServletRequest request) {
        return String.format(
                "Result %d, accessed by %s",
                a + b,
                request.getAttribute(ApiKeyConstant.REQUEST_ATTRIBUTE_USERNAME)
        );
    }
}
