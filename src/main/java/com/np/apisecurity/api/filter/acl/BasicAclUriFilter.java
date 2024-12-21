package com.np.apisecurity.api.filter.acl;

import com.np.apisecurity.api.auth.basic.BasicAuthApi;
import com.np.apisecurity.api.util.EncodeDecodeUtil;
import com.np.apisecurity.api.util.EncryptDecryptUtil;
import com.np.apisecurity.entity.basicauth.BasicAuthUser;
import com.np.apisecurity.repository.acl.BasicAclUriRepository;
import com.np.apisecurity.repository.basicauth.BasicAuthUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Optional;
import java.util.regex.Pattern;

//@Configuration
@Order(1)
public class BasicAclUriFilter extends OncePerRequestFilter {

    private static final String BASIC_AUTH = "Basic ";

    private final BasicAuthUserRepository basicAuthUserRepository;
    private final BasicAclUriRepository basicAclUriRepository;

    public BasicAclUriFilter(BasicAuthUserRepository basicAuthUserRepository,
                             BasicAclUriRepository basicAclUriRepository) {
        this.basicAuthUserRepository = basicAuthUserRepository;
        this.basicAclUriRepository = basicAclUriRepository;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {

        var basicAuthStr = request.getHeader(HttpHeaders.AUTHORIZATION);
        var authStr = basicAuthStr.substring(BASIC_AUTH.length());
        var usernamePasswordJoined = EncodeDecodeUtil.decodeBase64(authStr);
        var userCredentials = usernamePasswordJoined.split(":");
        try {
            String encryptedUsername = EncryptDecryptUtil.encryptAes(userCredentials[0], BasicAuthApi.SECRET_KEY);
            Optional<BasicAuthUser> userOptional = basicAuthUserRepository.findByUsername(encryptedUsername);
            BasicAuthUser basicAuthUser = userOptional.get();
            if (canAccessUri(request.getMethod(), request.getRequestURI(), basicAuthUser)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().write("Forbidden");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean canAccessUri(String method, String requestURI, BasicAuthUser basicAuthUser) {
//        for (var uriRef : basicAuthUser.getAllowedUris()) {
//            var allowedUriOptional = basicAclUriRepository.findById(uriRef.getUriId());
//            if (allowedUriOptional.isEmpty()) {
//                continue;
//            }
//
//            var allowedUri = allowedUriOptional.get();
//            if (StringUtils.equalsIgnoreCase(allowedUri.getMethod(), method) &&
//                    Pattern.matches(allowedUri.getUri(), requestURI)) {
//                return true;
//            }
//        }
//
//        return false;
        return false;
    }
}
