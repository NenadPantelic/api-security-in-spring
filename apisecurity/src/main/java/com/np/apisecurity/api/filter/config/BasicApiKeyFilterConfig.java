package com.np.apisecurity.api.filter.config;

import com.np.apisecurity.api.filter.apikey.BasicApiKeyFilter;
import com.np.apisecurity.repository.apikey.BasicApiKeyRepository;
import com.np.apisecurity.repository.basicauth.BasicAuthUserRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicApiKeyFilterConfig {

    private final BasicAuthUserRepository basicAuthUserRepository;
    private final BasicApiKeyRepository basicApiKeyRepository;

    public BasicApiKeyFilterConfig(BasicAuthUserRepository basicAuthUserRepository,
                                   BasicApiKeyRepository basicApiKeyRepository) {
        this.basicAuthUserRepository = basicAuthUserRepository;
        this.basicApiKeyRepository = basicApiKeyRepository;
    }


    @Bean
    public FilterRegistrationBean<BasicApiKeyFilter> apiKeyFilter() {
        var registrationBean = new FilterRegistrationBean<BasicApiKeyFilter>();
        registrationBean.setFilter(new BasicApiKeyFilter(basicAuthUserRepository, basicApiKeyRepository));
        registrationBean.addUrlPatterns("/api/auth/api-key/v1/add");
        return registrationBean;
    }
}
