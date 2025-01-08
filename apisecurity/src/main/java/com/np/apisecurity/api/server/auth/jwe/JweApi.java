package com.np.apisecurity.api.server.auth.jwe;

import com.np.apisecurity.constant.JwtConstant;
import com.np.apisecurity.constant.SessionCookieConstant;
import com.np.apisecurity.dto.internal.JweData;
import com.np.apisecurity.service.JweService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth/jwe/v1")
public class JweApi {

    private final JweService jweService;

    public JweApi(JweService jweService) {
        this.jweService = jweService;
    }

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public String login(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(JwtConstant.REQUEST_ATTRIBUTE_USERNAME);
        var jwe = new JweData(encryptedUsername);
        return jweService.store(jwe);
    }

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTime(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME);
        return String.format("Now is %s. Accessed by %s", LocalDateTime.now(), encryptedUsername);
    }
}
