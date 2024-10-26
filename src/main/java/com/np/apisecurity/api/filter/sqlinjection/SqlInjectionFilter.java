package com.np.apisecurity.api.filter.sqlinjection;

import com.np.apisecurity.api.filter.CachedBodyHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
//@Component
public class SqlInjectionFilter extends OncePerRequestFilter {

    private static final String[] SQL_REGEX_FILTERS = new String[]{
            "(?i)(.*)(\\b)+SELECT(\\b)+\\s.*(\\b)+FROM(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+DROP(\\b)+\\s.*(.*)"
    };

    private final List<Pattern> sqlValidationPatterns;

    public SqlInjectionFilter() {
        this.sqlValidationPatterns = Arrays.stream(SQL_REGEX_FILTERS)
                .map(sqlFilter -> Pattern.compile(sqlFilter, Pattern.CASE_INSENSITIVE))
                .collect(Collectors.toList());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Filtering the SQL expression against the malicious expressions");
        var cachedRequest = new CachedBodyHttpServletRequest(request);

        // against query string
        // decode parses the string format (in this case the URL)
        var queryString = URLDecoder.decode(
                Optional.ofNullable(cachedRequest.getQueryString()).orElse(""), StandardCharsets.UTF_8
        );
        if (containsSqlInjectionExpression(queryString)) {
            setErrorResponse(response);
            return;
        }

        // against path variable
        var pathVariable = URLDecoder.decode(
                Optional.ofNullable(cachedRequest.getRequestURI()).orElse(""), StandardCharsets.UTF_8
        );
        if (containsSqlInjectionExpression(pathVariable)) {
            setErrorResponse(response);
            return;
        }

        // against the request body
        var requestBody = IOUtils.toString(
                cachedRequest.getReader()).replaceAll("\\r\\n|\\r|\\n",
                ALREADY_FILTERED_SUFFIX
        );
        if (containsSqlInjectionExpression(requestBody)) {
            setErrorResponse(response);
            return;
        }

        filterChain.doFilter(cachedRequest, response);
    }

    private boolean containsSqlInjectionExpression(String sqlExpressionToValidate) {
        if (sqlExpressionToValidate == null || sqlExpressionToValidate.isBlank()) {
            return false;
        }

        for (Pattern validationPattern : sqlValidationPatterns) {
            if (validationPattern.matcher(sqlExpressionToValidate).find()) {
                return true;
            }
        }

        return false;
    }

    private void setErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().print("Bad request, SQL injection detected");
    }
}
