package com.np.apisecurity.api.filter.redis;

import com.np.apisecurity.constant.RedisConstant;
import com.np.apisecurity.dto.internal.RedisToken;
import com.np.apisecurity.service.RedisTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class RedisTokenFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    private final RedisTokenService tokenService;

    public RedisTokenFilter(RedisTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isValidToken(request)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().write("Invalid credential");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidToken(HttpServletRequest request) {
        var bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(bearerToken) || !bearerToken.startsWith(BEARER)) {
            return false;
        }

        var token = bearerToken.substring(BEARER.length());
        Optional<RedisToken> tokenOptional = tokenService.read(token);
        if (tokenOptional.isPresent()) {
            request.setAttribute(RedisConstant.REQUEST_ATTRIBUTE_USERNAME, tokenOptional.get().getUsername());
            return true;
        }

        return false;
    }
}