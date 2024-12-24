package com.np.apisecurity.api.filter.jwe;

import com.np.apisecurity.constant.JwtConstant;
import com.np.apisecurity.dto.internal.JweData;
import com.np.apisecurity.service.JweService;
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
public class JweFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    private final JweService jweService;

    public JweFilter(JweService jweService) {
        this.jweService = jweService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isValidJwe(request)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().write("Invalid JWT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidJwe(HttpServletRequest request) {
        var bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(bearerToken) || !bearerToken.startsWith(BEARER)) {
            return false;
        }

        var jwe = bearerToken.substring(BEARER.length());
        Optional<JweData> jweDataOptional = jweService.read(jwe);
        if (jweDataOptional.isPresent()) {
            request.setAttribute(JwtConstant.REQUEST_ATTRIBUTE_USERNAME, jweDataOptional.get().getUsername());
            return true;
        }

        return false;
    }
}