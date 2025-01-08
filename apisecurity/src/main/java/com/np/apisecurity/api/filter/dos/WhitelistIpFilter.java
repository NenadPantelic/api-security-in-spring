package com.np.apisecurity.api.filter.dos;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class WhitelistIpFilter extends OncePerRequestFilter {

    private static final String[] ALLOWED_IPS = {
            "0:0:0:0:0:0:0:1", // localhost
            "127.0.0.1", // localhost
            "24.135.150.70"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!ArrayUtils.contains(ALLOWED_IPS, request.getRemoteAddr())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().print(String.format("Forbidden IP: %s", request.getRemoteAddr()));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
