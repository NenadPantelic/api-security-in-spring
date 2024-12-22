package com.np.apisecurity.api.filter.config;

import com.np.apisecurity.api.filter.jwt.JwtAuthFilter;
import com.np.apisecurity.api.filter.jwt.JwtFilter;
import com.np.apisecurity.repository.basicauth.BasicAuthUserRepository;
import com.np.apisecurity.service.JwtService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class JwtFilterConfig {

    private final BasicAuthUserRepository basicAuthUserRepository;
    private final JwtService jwtService;

    public JwtFilterConfig(BasicAuthUserRepository basicAuthUserRepository,
                           JwtService jwtService) {
        this.basicAuthUserRepository = basicAuthUserRepository;
        this.jwtService = jwtService;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtAuthFilterFilterRegistrationBean() {
        var registrationBean = new FilterRegistrationBean<JwtAuthFilter>();
        registrationBean.setFilter(new JwtAuthFilter(basicAuthUserRepository));
        registrationBean.addUrlPatterns("/api/auth/jwt/v1/login");
        return registrationBean;
    }


    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        var registrationBean = new FilterRegistrationBean<JwtFilter>();
        registrationBean.setFilter(new JwtFilter(jwtService));
        registrationBean.addUrlPatterns(
                "/api/auth/jwt/v1/time",
                "/api/auth/jwt/v1/logout"
        );
        return registrationBean;
    }
}
