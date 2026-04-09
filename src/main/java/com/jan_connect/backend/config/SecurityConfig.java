package com.jan_connect.backend.config;

import com.jan_connect.backend.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationSuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // ── Fully Public (no token needed) ────────────────────────
                .requestMatchers(
                    "/api/auth/**",
                    "/api/states/**",
                    "/api/cities/**",
                    "/swagger-ui/**",
                    "/api-docs/**"
                ).permitAll()

                // ── Public READ operations (guests can browse) ────────────
                .requestMatchers(HttpMethod.GET,
                    "/api/cities/*/posts",
                    "/api/posts/*",
                    "/api/posts/*/comments",
                    "/api/cities/*/poll"
                ).permitAll()

                // ── Blocked for guests — requires authenticated user ───────
                // Any POST, PATCH, DELETE requires at minimum EMAIL_USER or OAUTH_USER
                .requestMatchers(HttpMethod.POST,
                    "/api/cities/*/posts",
                    "/api/posts/*/vote",
                    "/api/posts/*/comments",
                    "/api/cities/*/poll/vote",
                    "/api/cities/*/complaints",
                    "/api/upload/**"
                ).hasAnyRole("EMAIL_USER", "OAUTH_USER", "CITY_ADMIN", "SUPER_ADMIN")

                .requestMatchers(HttpMethod.DELETE,
                    "/api/posts/*",
                    "/api/posts/*/vote",
                    "/api/comments/*"
                ).hasAnyRole("EMAIL_USER", "OAUTH_USER", "CITY_ADMIN", "SUPER_ADMIN")

                // ── Admin-only ────────────────────────────────────────────
                .requestMatchers("/api/admin/**")
                    .hasAnyRole("CITY_ADMIN", "SUPER_ADMIN")

                .requestMatchers(HttpMethod.PATCH,
                    "/api/complaints/*/status"
                ).hasAnyRole("CITY_ADMIN", "SUPER_ADMIN")

                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2SuccessHandler)
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}