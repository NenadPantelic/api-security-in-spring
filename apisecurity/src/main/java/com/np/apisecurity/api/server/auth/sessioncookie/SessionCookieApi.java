package com.np.apisecurity.api.server.auth.sessioncookie;

import com.np.apisecurity.constant.SessionCookieConstant;
import com.np.apisecurity.dto.internal.SessionCookieToken;
import com.np.apisecurity.service.SessionCookieTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth/session-cookie/v1")
public class SessionCookieApi {

    private final SessionCookieTokenService tokenService;

    public SessionCookieApi(SessionCookieTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public String login(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME);
        var token = new SessionCookieToken(encryptedUsername);
        return tokenService.store(request, token);
    }

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTime(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME);
        return String.format("Now is %s. Accessed by %s", LocalDateTime.now(), encryptedUsername);
    }

    @DeleteMapping(value = "/logout", produces = MediaType.TEXT_PLAIN_VALUE)
    public void logout(HttpServletRequest request) {
        tokenService.delete(request);
    }
}
