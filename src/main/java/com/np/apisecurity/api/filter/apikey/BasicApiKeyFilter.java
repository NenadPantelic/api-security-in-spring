package com.np.apisecurity.api.filter.apikey;

import com.np.apisecurity.constant.ApiKeyConstant;
import com.np.apisecurity.entity.apikey.BasicApiKey;
import com.np.apisecurity.repository.apikey.BasicApiKeyRepository;
import com.np.apisecurity.repository.basicauth.BasicAuthUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
public class BasicApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-Api-Key";

    private final BasicAuthUserRepository userRepository;
    private final BasicApiKeyRepository apiKeyRepository;

    public BasicApiKeyFilter(BasicAuthUserRepository userRepository, BasicApiKeyRepository apiKeyRepository) {
        this.userRepository = userRepository;
        this.apiKeyRepository = apiKeyRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var apiKey = request.getHeader(API_KEY_HEADER);
        if (isValidApiKey(apiKey, request)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().write("Invalid API key.");
        }
    }

    private boolean isValidApiKey(String apiKey, HttpServletRequest request) {
        Optional<BasicApiKey> apiKeyOptional = apiKeyRepository.findByApiKeyAndExpiresAtAfter(
                apiKey, LocalDateTime.now()
        );
        if (apiKeyOptional.isEmpty()) {
            return false;
        }

        var userId = apiKeyOptional.get().getUserId();
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }

        request.setAttribute(ApiKeyConstant.REQUEST_ATTRIBUTE_USERNAME, userOptional.get().getUsername());
        return true;
    }
}
