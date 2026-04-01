package com.jan_connect.backend.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.acess-token-expiration}")
    private long acessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Value("${jwt.guest-token-expiration}")
    private long guestTokenExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generatAcessToken(UserDetails userDetails) {
        return buildToken(userDetails.getUsername(), acessTokenExpiration, Map.of("type", "acess"));
    }

    public String generateRefreshTokenValue() {
        return UUID.randomUUID().toString();
    }

    public String generateGuestToken(String guestId) {
        return buildToken(guestId, guestTokenExpiration, Map.of("type", "guest"));
    }

    private String buildToken(String subject, long expiration, Map<String, Object> claims){

        return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt((new Date()))
        .setExpiration(new Date(System.currentTimeMillis()+expiration))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    public String extractUsername(String token){
        return Jwts.parserBuilder()
        .setSigningKey(getSigningKey()).build()
        .parseClaimsJws(token).getBody().getSubject();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails){
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token){
        return Jwts.parserBuilder()
        .setSigningKey(getSigningKey()).build()
        .parseClaimsJws(token).getBody()
        .getExpiration().before(new Date());
    }
}
