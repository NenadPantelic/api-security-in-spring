package com.np.apisecurity.api.filter.config;

import com.google.common.util.concurrent.RateLimiter;
import com.np.apisecurity.api.filter.dos.RateLimitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RateLimitFilterConfig {

//    @Bean
//    public FilterRegistrationBean<RateLimitFilter> rateLimitFilterBlue() {
//        var registrationBean = new FilterRegistrationBean<RateLimitFilter>();
//        registrationBean.setFilter(new RateLimitFilter(3));
//        registrationBean.setName("rateLimitFilterBlue");
//        registrationBean.addUrlPatterns("/api/dos/v1/blue");
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean<RateLimitFilter> rateLimitFilterRed() {
//        var registrationBean = new FilterRegistrationBean<RateLimitFilter>();
//        registrationBean.setFilter(new RateLimitFilter(2));
//        registrationBean.setName("rateLimitFilterRed");
//        registrationBean.addUrlPatterns("/api/dos/v1/red");
//        return registrationBean;
//    }
}
