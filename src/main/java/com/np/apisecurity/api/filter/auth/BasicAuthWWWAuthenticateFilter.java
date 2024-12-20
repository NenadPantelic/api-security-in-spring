package com.np.apisecurity.api.filter.auth;

import com.np.apisecurity.api.auth.basic.BasicAuthApi;
import com.np.apisecurity.api.util.EncodeDecodeUtil;
import com.np.apisecurity.api.util.EncryptDecryptUtil;
import com.np.apisecurity.api.util.HashUtil;
import com.np.apisecurity.entity.basicauth.BasicAuthUser;
import com.np.apisecurity.repository.basicauth.BasicAuthUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Optional;

@Slf4j
//@Component
public class BasicAuthWWWAuthenticateFilter extends OncePerRequestFilter {

    private static final String BASIC_AUTH = "Basic ";
    private static final String BASIC_AUTH_WWW_AUTHENTICATE = "Basic realm=\"Need valid credential to access this API\"";

    private final BasicAuthUserRepository basicAuthUserRepository;

    public BasicAuthWWWAuthenticateFilter(BasicAuthUserRepository basicAuthUserRepository) {
        this.basicAuthUserRepository = basicAuthUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var basicAuthToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (isValidBasicAuth(basicAuthToken)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setHeader(HttpHeaders.WWW_AUTHENTICATE, BASIC_AUTH_WWW_AUTHENTICATE);
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().write("Invalid credential");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidBasicAuth(String basicAuthToken) throws
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        if (!basicAuthToken.startsWith(BASIC_AUTH)) {
            return false;
        }

        String authStr = basicAuthToken.substring(BASIC_AUTH.length());
        String usernamePasswordJoined = EncodeDecodeUtil.decodeBase64(authStr);
        String[] userCredentials = usernamePasswordJoined.split(":");

        String encryptedUsername = EncryptDecryptUtil.encryptAes(userCredentials[0], BasicAuthApi.SECRET_KEY);
        Optional<BasicAuthUser> user = basicAuthUserRepository.findByUsername(encryptedUsername);
        return user.filter(
                        basicAuthUser -> HashUtil.isBcryptMatch(userCredentials[1], basicAuthUser.getPasswordHash()))
                .isPresent();
    }
}
