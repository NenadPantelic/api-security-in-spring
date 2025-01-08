package com.np.apisecurity.api.server.auth.jwt;

import com.np.apisecurity.constant.JwtConstant;
import com.np.apisecurity.constant.SessionCookieConstant;
import com.np.apisecurity.dto.internal.JwtData;
import com.np.apisecurity.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth/jwt/v1")
public class JwtApi {

    private final JwtService jwtService;

    public JwtApi(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public String login(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(JwtConstant.REQUEST_ATTRIBUTE_USERNAME);
        var jwt = new JwtData(encryptedUsername);
        return jwtService.store(jwt);
    }

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTime(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME);
        return String.format("Now is %s. Accessed by %s", LocalDateTime.now(), encryptedUsername);
    }
}
