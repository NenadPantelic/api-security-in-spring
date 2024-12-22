package com.np.apisecurity.api.filter.config;

import com.np.apisecurity.api.filter.redis.RedisAuthFilter;
import com.np.apisecurity.api.filter.redis.RedisTokenFilter;
import com.np.apisecurity.repository.basicauth.BasicAuthUserRepository;
import com.np.apisecurity.service.RedisTokenService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class RedisTokenFilterConfig {

    private final BasicAuthUserRepository basicAuthUserRepository;
    private final RedisTokenService tokenService;

    public RedisTokenFilterConfig(BasicAuthUserRepository basicAuthUserRepository,
                                  RedisTokenService tokenService) {
        this.basicAuthUserRepository = basicAuthUserRepository;
        this.tokenService = tokenService;
    }

    @Bean
    public FilterRegistrationBean<RedisAuthFilter> redisAuthFilter() {
        var registrationBean = new FilterRegistrationBean<RedisAuthFilter>();
        registrationBean.setFilter(new RedisAuthFilter(basicAuthUserRepository));
        registrationBean.addUrlPatterns("/api/auth/redis/v1/login");
        return registrationBean;
    }


    @Bean
    public FilterRegistrationBean<RedisTokenFilter> redisTokenFilter() {
        var registrationBean = new FilterRegistrationBean<RedisTokenFilter>();
        registrationBean.setFilter(new RedisTokenFilter(tokenService));
        registrationBean.addUrlPatterns(
                "/api/auth/redis/v1/time",
                "/api/auth/redis/v1/logout"
        );
        return registrationBean;
    }
}
