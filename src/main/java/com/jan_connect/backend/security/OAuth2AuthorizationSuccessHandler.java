package com.jan_connect.backend.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.jan_connect.backend.security.JwtUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthorizationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();
        org.springframework.security.core.userdetails.UserDetails userDetails;
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails ud) {
            userDetails = ud;
        } else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            userDetails = org.springframework.security.core.userdetails.User.withUsername(email != null ? email : oauth2User.getName())
                    .password("")
                    .authorities("ROLE_CITIZEN")
                    .build();
        } else {
            userDetails = org.springframework.security.core.userdetails.User.withUsername(authentication.getName())
                    .password("")
                    .authorities("ROLE_CITIZEN")
                    .build();
        }

        String token = jwtUtil.generateAccessToken(userDetails);
        response.setHeader("Authorization", "Bearer " + token);
    }
}