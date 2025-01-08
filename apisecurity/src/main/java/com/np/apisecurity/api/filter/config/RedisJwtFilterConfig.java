package com.np.apisecurity.api.filter.config;

import com.np.apisecurity.api.filter.redisjwt.RedisJwtAuthFilter;
import com.np.apisecurity.api.filter.redisjwt.RedisJwtFilter;
import com.np.apisecurity.repository.basicauth.BasicAuthUserRepository;
import com.np.apisecurity.service.RedisJwtService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RedisJwtFilterConfig {

    private final BasicAuthUserRepository basicAuthUserRepository;
    private final RedisJwtService redisJwtService;

    public RedisJwtFilterConfig(BasicAuthUserRepository basicAuthUserRepository,
                                RedisJwtService redisJwtService) {
        this.basicAuthUserRepository = basicAuthUserRepository;
        this.redisJwtService = redisJwtService;
    }

    @Bean
    public FilterRegistrationBean<RedisJwtAuthFilter> redisJwtAuthFilterFilterRegistrationBean() {
        var registrationBean = new FilterRegistrationBean<RedisJwtAuthFilter>();
        registrationBean.setFilter(new RedisJwtAuthFilter(basicAuthUserRepository));
        registrationBean.addUrlPatterns("/api/auth/redis-jwt/v1/login");
        return registrationBean;
    }


    @Bean
    public FilterRegistrationBean<RedisJwtFilter> redisJwtFilter() {
        var registrationBean = new FilterRegistrationBean<RedisJwtFilter>();
        registrationBean.setFilter(new RedisJwtFilter(redisJwtService));
        registrationBean.addUrlPatterns(
                "/api/auth/redis-jwt/v1/time",
                "/api/auth/redis-jwt/v1/logout"
        );
        return registrationBean;
    }
}
