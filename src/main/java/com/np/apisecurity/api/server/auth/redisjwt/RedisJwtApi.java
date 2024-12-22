package com.np.apisecurity.api.server.auth.redisjwt;

import com.np.apisecurity.constant.JwtConstant;
import com.np.apisecurity.constant.SessionCookieConstant;
import com.np.apisecurity.dto.internal.JwtData;
import com.np.apisecurity.service.RedisJwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth/redis-jwt/v1")
public class RedisJwtApi {

    private final RedisJwtService redisJwtService;

    public RedisJwtApi(RedisJwtService redisJwtService) {
        this.redisJwtService = redisJwtService;
    }

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public String login(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(JwtConstant.REQUEST_ATTRIBUTE_USERNAME);
        var jwt = new JwtData(encryptedUsername);
        return redisJwtService.store(jwt);
    }

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTime(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME);
        return String.format("Now is %s. Accessed by %s", LocalDateTime.now(), encryptedUsername);
    }

    @DeleteMapping(value = "/logout", produces = MediaType.TEXT_PLAIN_VALUE)
    public void logout(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        var jwt = authorizationHeader.substring("Bearer ".length());
        redisJwtService.delete(jwt);
    }
}
