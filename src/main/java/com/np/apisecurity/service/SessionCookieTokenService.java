package com.np.apisecurity.service;

import com.np.apisecurity.api.util.HashUtil;
import com.np.apisecurity.api.util.SecureStringUtil;
import com.np.apisecurity.constant.SessionCookieConstant;
import com.np.apisecurity.dto.internal.SessionCookieToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Slf4j
@Service
public class SessionCookieTokenService {

    public String store(HttpServletRequest request, SessionCookieToken sessionCookieToken) {
//        var session = request.getSession(true); // true means that the session will be created if it not exists (prone
//        to session fixation attack
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // create a new session; every time we get a new token that should be used in X-CSRF header
        session = request.getSession(true);
        session.setAttribute(SessionCookieConstant.SESSION_ATTRIBUTE_USERNAME, sessionCookieToken.getUsername());
        // do not return the plaintext session id; prone to CSRF
        try {
            return HashUtil.sha256(session.getId(), sessionCookieToken.getUsername());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    public Optional<SessionCookieToken> readTokenFromStore(HttpServletRequest request, String tokenId) {
        var session = request.getSession(false); // do not create a session if it not exists

        if (session == null) {
            return Optional.empty();
        }

        var username = (String) session.getAttribute(SessionCookieConstant.SESSION_ATTRIBUTE_USERNAME);
        try {
            var computedTokenId = HashUtil.sha256(session.getId(), username);
            var token = new SessionCookieToken(username);
            return SecureStringUtil.equals(computedTokenId, tokenId) ? Optional.of(token) : Optional.empty();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void delete(HttpServletRequest request) {
        var session = request.getSession(false);
        session.invalidate();
    }
}
