package com.np.apisecurity.api.server.auth.redis;

import com.np.apisecurity.constant.RedisConstant;
import com.np.apisecurity.constant.SessionCookieConstant;
import com.np.apisecurity.dto.internal.RedisToken;
import com.np.apisecurity.service.RedisTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth/redis/v1")
public class RedisApi {

    private final RedisTokenService tokenService;

    public RedisApi(RedisTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public String login(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(RedisConstant.REQUEST_ATTRIBUTE_USERNAME);
        var token = new RedisToken(encryptedUsername);
        return tokenService.store(token);
    }

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTime(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME);
        return String.format("Now is %s. Accessed by %s", LocalDateTime.now(), encryptedUsername);
    }

    @DeleteMapping(value = "/logout", produces = MediaType.TEXT_PLAIN_VALUE)
    public void logout(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        var tokenId = authorizationHeader.substring("Bearer ".length());
        tokenService.delete(tokenId);
    }
}
