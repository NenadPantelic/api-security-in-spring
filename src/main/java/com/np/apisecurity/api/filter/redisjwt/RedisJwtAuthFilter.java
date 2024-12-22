package com.np.apisecurity.api.filter.redisjwt;

import com.np.apisecurity.api.server.auth.basic.BasicAuthApi;
import com.np.apisecurity.api.util.EncodeDecodeUtil;
import com.np.apisecurity.api.util.EncryptDecryptUtil;
import com.np.apisecurity.api.util.HashUtil;
import com.np.apisecurity.constant.SessionCookieConstant;
import com.np.apisecurity.entity.basicauth.BasicAuthUser;
import com.np.apisecurity.repository.basicauth.BasicAuthUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Optional;

public class RedisJwtAuthFilter extends OncePerRequestFilter {

    private static final String BASIC_AUTH = "Basic ";

    private final BasicAuthUserRepository basicAuthUserRepository;

    public RedisJwtAuthFilter(BasicAuthUserRepository basicAuthUserRepository) {
        this.basicAuthUserRepository = basicAuthUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var basicAuthToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (isValidBasicAuth(basicAuthToken, request)) {
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

    private boolean isValidBasicAuth(String basicAuthToken, HttpServletRequest request) throws
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        if (!basicAuthToken.startsWith(BASIC_AUTH)) {
            return false;
        }

        String authStr = basicAuthToken.substring(BASIC_AUTH.length());
        String usernamePasswordJoined = EncodeDecodeUtil.decodeBase64(authStr);
        String[] userCredentials = usernamePasswordJoined.split(":");

        String encryptedUsername = EncryptDecryptUtil.encryptAes(userCredentials[0], BasicAuthApi.SECRET_KEY);
        Optional<BasicAuthUser> userOptional = basicAuthUserRepository.findByUsername(encryptedUsername);
        if (userOptional.isEmpty()) {
            return false;
        }

        BasicAuthUser user = userOptional.get();
        if (HashUtil.isBcryptMatch(userCredentials[1], user.getPasswordHash())) {
            request.setAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME, encryptedUsername);
            return true;
        }

        return false;
    }
}
