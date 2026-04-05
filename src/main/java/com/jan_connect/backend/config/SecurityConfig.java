package com.jan_connect.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jan_connect.backend.security.JWTAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final OAuth2AuthorizationSuccessHandler oAuth2AuthorizationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth.requestMatchers(
            // no token apis
            "/api/auth/**",
            "api/states/**",
            "api/cities/**",
            "/swagger-ui/**",
            "/api/docs/**"
        ).permitAll()
        .requestMatchers(
            // Public read operations
            HttpMethod.GET,
            "/api/cities/*/posts",
            "/api/posts/*",
            "/api/posts/*/comments",
            "/api/cities/*/poll"
        ).permitAll()
        .requestMatchers(
            // blocked for guests / requires email or oauth
            HttpMethod.POST,
            "/api/cities/*/posts",
            "/api/posts/*/comments",
            "/api/posts/*/vote",
            "/api/cities/*/poll/vote",
            "/api/cities/*/complaints",
            "/api/upload/**"
        ).hasAnyRole("EMAIL_USER", "OAUTH_USER", "CITY_ADMIN", "SUPER_ADMIN")
        .requestMatchers(
            HttpMethod.DELETE,
            "/api/posts/*",
            "/api/comments/*",
            "/api/posts/*/vote"
        ).hasAnyRole("EMAIL_USER", "OAUTH_USER", "CITY_ADMIN", "SUPER_ADMIN")
        // Admin only
        .requestMatchers(
            "/api/admin/**"
        ).hasAnyRole("CITY_ADMIN","SUPER_ADMIN")
        .requestMatchers(
            HttpMethod.PATCH,
            "/api/complaints/*/status"
        ).hasAnyRole("CITY_ADMIN", "SUPER_ADMIN")
        .anyRequest().authenticated()
        ).oauth2Login(oauth2 -> oauth2.successHandler(oAuth2AuthorizationSuccessHandler))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authentcationManager(AuthenticationConfiguration config) th
}
