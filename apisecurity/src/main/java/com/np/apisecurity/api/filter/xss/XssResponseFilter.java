package com.np.apisecurity.api.filter.xss;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// NOTE: very simple filter, must be fine-tuned for the production application
//@Component
public class XssResponseFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("X-XSS-Protection", "0");
        response.setHeader("X-Content-Type-Options", "nosniff"); // instructing browser to follow the content type
        // headers that the server sends and not to sniff them
        // trust only the localhost, but the inline scripts demand a different treatment
        // CSP nonce should be used only once
        // in production code the JS code is usually an independent layer (JS scripts are imported by HTML), so using
        // self is fine
        // self checks the protocol, host and port matching - it must be identical
        response.setHeader("Content-Security-Policy", "script-src 'self' 'nonce-someRand0mNonc3';");

        filterChain.doFilter(request, response);
    }
}
