package com.np.apisecurity.api.filter.hmac;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.apisecurity.api.filter.CachedBodyHttpServletRequest;
import com.np.apisecurity.api.server.encodedecode.HmacApi;
import com.np.apisecurity.api.util.HmacNonceStorage;
import com.np.apisecurity.api.util.HmacUtil;
import com.np.apisecurity.dto.request.HmacData;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class HmacFilter extends OncePerRequestFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String REGISTER_DATE_HEADER = "X-Register-Date";
    private static final String HMAC_HEADER = "X-Hmac";
    private static final String HMAC_NONCE = "X-Nonce";


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Filtering the HMAC...");
        var cachedRequest = new CachedBodyHttpServletRequest(request);
        var nonce = request.getHeader(HMAC_NONCE);

        try {
            if (isValidHmac(cachedRequest, nonce) && HmacNonceStorage.addWhenNotExists(nonce)) {
                filterChain.doFilter(cachedRequest, response);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().print("Invalid HMAC");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidHmac(CachedBodyHttpServletRequest cachedHttpRequest,
                                String nonce) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        var requestBody = OBJECT_MAPPER.readValue(cachedHttpRequest.getReader(), HmacData.class);
        var registerDate = cachedHttpRequest.getHeader(REGISTER_DATE_HEADER);
        var hmacFromClient = cachedHttpRequest.getHeader(HMAC_HEADER);

        var hmacMessage = HmacApi.constructHmacMessage(
                cachedHttpRequest.getMethod(),
                cachedHttpRequest.getRequestURI(),
                registerDate,
                nonce,
                requestBody
        );

        return HmacUtil.doHmacMatch(hmacMessage, HmacApi.SECRET_KEY, hmacFromClient);
    }
}
