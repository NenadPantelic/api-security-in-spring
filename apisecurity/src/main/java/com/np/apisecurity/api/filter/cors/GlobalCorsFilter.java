package com.np.apisecurity.api.filter.cors;

import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalCorsFilter extends CorsFilter {


    public GlobalCorsFilter() {
        super(corsConfiguration());
    }

    private static UrlBasedCorsConfigurationSource corsConfiguration() {
        var config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://127.0.0.1:8080");
        config.addAllowedOrigin("http://192.168.0.13:8080");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
