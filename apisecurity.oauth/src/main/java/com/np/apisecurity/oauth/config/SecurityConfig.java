package com.np.apisecurity.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // for preauthorize annotation
public class SecurityConfig {

    private static final String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

    @Autowired
    private Environment env;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {

        List<ClientRegistration> clientRegistrations = new ArrayList<>();
        clientRegistrations.add(getRegistration("okta"));
        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }

    private ClientRegistration getRegistration(String client) {
        String clientId = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-id");

        if (clientId == null) {
            return null;
        }

        String clientSecret = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-secret");

        return CommonOAuth2Provider.OKTA
                .getBuilder(client)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
////                .authorizeHttpRequests((authz) -> authz
//                .requestMatchers("/**").authenticated()
//                .requestMatchers("/api/math/random").permitAll()
//                .anyRequest().authenticated().and().oauth2Login(Customizer.withDefaults());
//        return http.build();
//        // https://www.baeldung.com/spring-security-5-oauth2-login
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers("/**").authenticated()
                .requestMatchers("/api/math/random").permitAll()
                .and()
//                .clientRegistrationRepository(clientRegistrationRepository())
                .oauth2Login(Customizer.withDefaults());

//                .authorizedClientService(authorizedClientService());
        return http.build();
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

}
