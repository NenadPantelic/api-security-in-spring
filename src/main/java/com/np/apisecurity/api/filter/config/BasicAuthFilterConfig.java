package com.np.apisecurity.api.filter.config;

import com.np.apisecurity.api.filter.auth.BasicAuthFilter;
import com.np.apisecurity.repository.basicauth.BasicAuthUserRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicAuthFilterConfig {

    private final BasicAuthUserRepository basicAuthUserRepository;

    public BasicAuthFilterConfig(BasicAuthUserRepository basicAuthUserRepository) {
        this.basicAuthUserRepository = basicAuthUserRepository;
    }

//    @Bean
//    public FilterRegistrationBean<BasicAuthFilter> basicAuthFilter() {
//        var registrationBean = new FilterRegistrationBean<BasicAuthFilter>();
//        registrationBean.setFilter(new BasicAuthFilter(basicAuthUserRepository));
//        registrationBean.addUrlPatterns("/api/auth/basic/v1/time");
//        return registrationBean;
//    }

}
