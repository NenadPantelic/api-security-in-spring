package com.np.apisecurity.api.filter.config;

import com.np.apisecurity.api.filter.hmac.HmacFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HmacFilterConfig {

    @Bean
    public FilterRegistrationBean<HmacFilter> hmacFilter() {
        var registrationBean = new FilterRegistrationBean<HmacFilter>();
        registrationBean.setFilter(new HmacFilter());
        registrationBean.addUrlPatterns("/api/v1/hmac/info");
        return registrationBean;
    }

}
