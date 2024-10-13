package de.bensch.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/login", "/oauth2/**")
                .permitAll()
                .anyRequest()
                .authenticated()
        );

        http.oauth2Login(oauth2 -> oauth2
                .successHandler(new OAuth2LoginSuccessHandler()));
        return http.build();
    }
}
