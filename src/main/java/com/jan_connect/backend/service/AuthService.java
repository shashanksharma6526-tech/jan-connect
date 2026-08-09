package com.jan_connect.backend.service;

import com.jan_connect.backend.dto.auth.*;
import com.jan_connect.backend.entity.*;
import com.jan_connect.backend.enums.*;
import com.jan_connect.backend.exceptions.ResourceNotFoundException;
import com.jan_connect.backend.exceptions.UnauthorizedException;
import com.jan_connect.backend.repository.*;
import com.jan_connect.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // ── EMAIL REGISTRATION ────────────────────────────────────────────────
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("An account with this email already exists.");
        }

        City city = cityRepository.findByIdAndIsActiveTrue(request.getCityId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "City not found with ID: " + request.getCityId()));

        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .provider(AuthProvider.EMAIL)
            .type(UserType.EMAIL_USER)
            .city(city)
            .build();

        userRepository.save(user);
        return buildAuthResponse(user);
    }

    // ── EMAIL LOGIN ───────────────────────────────────────────────────────
    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
            )
        );

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return buildAuthResponse(user);
    }

    // ── OAUTH LOGIN (Google) ──────────────────────────────────────────────
    // "Upsert" means: create the user if they don't exist, or return them if they do.
    // This is the standard pattern for OAuth — you never ask the user to "register".
    @Transactional
    public AuthResponse oauthLogin(OAuthRequest request) {
        User user = userRepository
            .findByProviderAndProviderId(request.getProvider(), request.getProviderId())
            .orElseGet(() -> {
                // New OAuth user — create their account automatically
                City defaultCity = cityRepository.findByIdAndIsActiveTrue(request.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("City not found"));

                return userRepository.save(User.builder()
                    .username(request.getName())
                    .email(request.getEmail())
                    .provider(request.getProvider())
                    .providerId(request.getProviderId())
                    .avatar(request.getAvatarUrl())
                    .type(UserType.OAUTH_USER)
                    .city(defaultCity)
                    .build());
            });

        return buildAuthResponse(user);
    }

    // ── GUEST SESSION ─────────────────────────────────────────────────────
    // Creates a temporary guest user with a unique ID.
    // Guest tokens expire after 24 hours.
    // Guests can browse but the SecurityConfig blocks them from POST/PATCH/DELETE.
    @Transactional
    public AuthResponse createGuestSession() {
        String guestId = "guest_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        User guest = User.builder()
            .username(guestId)
            .provider(AuthProvider.GUEST)
            .type(UserType.GUEST)
            .build();

        userRepository.save(guest);

        String guestToken = jwtUtil.generateGuestToken(guestId);
        return new AuthResponse(guestToken, null, guestId, UserType.GUEST.name(), null);
    }

    // ── TOKEN REFRESH ─────────────────────────────────────────────────────
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken stored = refreshTokenRepository
            .findByTokenAndIsRevokedFalse(request.getRefreshToken())
            .orElseThrow(() -> new UnauthorizedException(
                "Refresh token is invalid or has been revoked."));

        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            stored.setIsRevoked(true);
            refreshTokenRepository.save(stored);
            throw new UnauthorizedException("Refresh token has expired. Please log in again.");
        }

        // Revoke the old refresh token and issue a new one (rotation)
        stored.setIsRevoked(true);
        refreshTokenRepository.save(stored);

        return buildAuthResponse(stored.getUser());
    }

    // ── LOGOUT ────────────────────────────────────────────────────────────
    @Transactional
    public void logout(String userEmail) {
        userRepository.findByEmail(userEmail).ifPresent(user ->
            refreshTokenRepository.deleteAllByUserId(user.getId())
        );
    }

    // ── HELPER ────────────────────────────────────────────────────────────
    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtUtil.generateAccessToken(user);

        // Create a new refresh token and persist it
        String refreshTokenValue = jwtUtil.generateRefreshTokenValue();
        RefreshToken refreshToken = RefreshToken.builder()
            .token(refreshTokenValue)
            .user(user)
            .isRevoked(false)
            .expiresAt(LocalDateTime.now().plusDays(30))
            .build();
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(
            accessToken,
            refreshTokenValue,
            user.getUsername(),
            user.getType().name(),
            user.getCity() != null ? user.getCity().getId() : null
        );
    }
}