package com.np.apisecurity.api.filter.config;

import com.np.apisecurity.api.filter.sessioncookie.SessionCookieBasicAuthFilter;
import com.np.apisecurity.api.filter.sessioncookie.SessionCookieTokenFilter;
import com.np.apisecurity.repository.basicauth.BasicAuthUserRepository;
import com.np.apisecurity.service.SessionCookieTokenService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

//@Configuration
public class SessionCookieFilterConfig {

    private final BasicAuthUserRepository basicAuthUserRepository;
    private final SessionCookieTokenService sessionCookieTokenService;

    public SessionCookieFilterConfig(BasicAuthUserRepository basicAuthUserRepository,
                                     SessionCookieTokenService sessionCookieTokenService) {
        this.basicAuthUserRepository = basicAuthUserRepository;
        this.sessionCookieTokenService = sessionCookieTokenService;
    }

    @Bean
    public FilterRegistrationBean<SessionCookieBasicAuthFilter> sessionCookieAuthFilter() {
        var registrationBean = new FilterRegistrationBean<SessionCookieBasicAuthFilter>();
        registrationBean.setFilter(new SessionCookieBasicAuthFilter(basicAuthUserRepository));
        registrationBean.addUrlPatterns("/api/auth/session-cookie/v1/login");
        return registrationBean;
    }


    @Bean
    public FilterRegistrationBean<SessionCookieTokenFilter> sessionCookieTokenFilter() {
        var registrationBean = new FilterRegistrationBean<SessionCookieTokenFilter>();
        registrationBean.setFilter(new SessionCookieTokenFilter(sessionCookieTokenService));
        registrationBean.addUrlPatterns(
                "/api/auth/session-cookie/v1/time",
                "/api/auth/session-cookie/v1/logout"
        );
        return registrationBean;
    }
}
