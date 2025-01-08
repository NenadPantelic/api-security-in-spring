package com.np.apisecurity.api.filter.sessioncookie;

import com.np.apisecurity.constant.SessionCookieConstant;
import com.np.apisecurity.service.SessionCookieTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SessionCookieTokenFilter extends OncePerRequestFilter {

    private static final String CSRF_TOKEN_HEADER = "X-CSRF";

    private final SessionCookieTokenService sessionCookieTokenService;

    public SessionCookieTokenFilter(SessionCookieTokenService sessionCookieTokenService) {
        this.sessionCookieTokenService = sessionCookieTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isValidSessionCookie(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().write("Invalid token.");
    }

    private boolean isValidSessionCookie(HttpServletRequest request) {
        var providedTokenId = request.getHeader(CSRF_TOKEN_HEADER);
        var token = sessionCookieTokenService.readTokenFromStore(request, providedTokenId);
        if (token.isPresent()) {
            request.setAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME, token.get().getUsername());
            return true;
        }

        return false;
    }
}
