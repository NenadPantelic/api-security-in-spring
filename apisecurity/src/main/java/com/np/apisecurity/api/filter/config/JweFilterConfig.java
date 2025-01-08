package com.np.apisecurity.api.filter.config;

import com.np.apisecurity.api.filter.jwe.JweAuthFilter;
import com.np.apisecurity.api.filter.jwe.JweFilter;
import com.np.apisecurity.repository.basicauth.BasicAuthUserRepository;
import com.np.apisecurity.service.JweService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JweFilterConfig {

    private final BasicAuthUserRepository basicAuthUserRepository;
    private final JweService jweService;

    public JweFilterConfig(BasicAuthUserRepository basicAuthUserRepository,
                           JweService jweService) {
        this.basicAuthUserRepository = basicAuthUserRepository;
        this.jweService = jweService;
    }

    @Bean
    public FilterRegistrationBean<JweAuthFilter> jweAuthFilterFilterRegistrationBean() {
        var registrationBean = new FilterRegistrationBean<JweAuthFilter>();
        registrationBean.setFilter(new JweAuthFilter(basicAuthUserRepository));
        registrationBean.addUrlPatterns("/api/auth/jwe/v1/login");
        return registrationBean;
    }


    @Bean
    public FilterRegistrationBean<JweFilter> jweFilter() {
        var registrationBean = new FilterRegistrationBean<JweFilter>();
        registrationBean.setFilter(new JweFilter(jweService));
        registrationBean.addUrlPatterns(
                "/api/auth/jwe/v1/time",
                "/api/auth/jwe/v1/logout"
        );
        return registrationBean;
    }
}
