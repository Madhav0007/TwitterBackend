package com.integ.task.service.impl;

import com.integ.task.config.JwtUtil;
import com.integ.task.dto.*;
import com.integ.task.entity.RefreshToken;
import com.integ.task.entity.UserRole;
import com.integ.task.exceptions.GlobalException;
import com.integ.task.repository.RefreshTokenRepository;
import com.integ.task.repository.UserRepository;
import com.integ.task.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private static final long REFRESH_TOKEN_TTL_MS = 7L * 24 * 60 * 60 * 1000; // 7 days

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthServiceImpl(JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        UserRole user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new GlobalException("Invalid username or password", 401, HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new GlobalException("Invalid username or password", 401, HttpStatus.UNAUTHORIZED);
        }

        revokeExistingRefreshTokens(user.getId());

        String accessToken = jwtUtil.generateAccessToken(user);
        RefreshToken refreshToken = createRefreshToken(user);

        return AuthResponseDto.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(900L)
                .userId(user.getId())
                .username(user.getUsername())
                .build();
    }

    @Override
    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new GlobalException("Invalid refresh token", 401, HttpStatus.UNAUTHORIZED));

        if (storedToken.isRevoked() || storedToken.getExpiryDate().isBefore(Instant.now())) {
            throw new GlobalException("Refresh token expired or revoked", 401, HttpStatus.UNAUTHORIZED);
        }

        UserRole user = storedToken.getUser();

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        String newAccessToken = jwtUtil.generateAccessToken(user);
        RefreshToken newRefreshToken = createRefreshToken(user);

        return AuthResponseDto.builder()
                .tokenType("Bearer")
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .expiresIn(900L)
                .userId(user.getId())
                .username(user.getUsername())
                .build();
    }

    @Override
    public void logout(RefreshTokenRequestDto request) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new GlobalException("Invalid refresh token", 401, HttpStatus.UNAUTHORIZED));

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);
    }

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        String username = request.getUsername().trim();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new GlobalException("Username already exists", 409, HttpStatus.CONFLICT);
        }

        return runWithSystemJwt(() -> {
            UserRole user = new UserRole();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole("USER");

            UserRole saved = userRepository.save(user);

            revokeExistingRefreshTokens(saved.getId());

            String accessToken = jwtUtil.generateAccessToken(saved);
            RefreshToken refreshToken = createRefreshToken(saved);

            return AuthResponseDto.builder()
                    .tokenType("Bearer")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .expiresIn(900L)
                    .userId(saved.getId())
                    .username(saved.getUsername())
                    .build();
        });
    }

    private RefreshToken createRefreshToken(UserRole user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(generateRefreshTokenValue());
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN_TTL_MS));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    private void revokeExistingRefreshTokens(Long userId) {
        List<RefreshToken> activeTokens = refreshTokenRepository.findAllByUser_IdAndRevokedFalse(userId);
        if (!activeTokens.isEmpty()) {
            activeTokens.forEach(t -> t.setRevoked(true));
            refreshTokenRepository.saveAll(activeTokens);
        }
    }

    private String generateRefreshTokenValue() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private <T> T runWithSystemJwt(java.util.function.Supplier<T> action) {
        var previousAuth = SecurityContextHolder.getContext().getAuthentication();

        try {
            Jwt jwt = new Jwt(
                    "system-token",
                    Instant.now(),
                    Instant.now().plusSeconds(3600),
                    Map.of("alg", "none"),
                    Map.of(
                            "userLoginIDP", 0L,
                            "careProviderIDP", 0L,
                            "userTypeIDP", 0L,
                            "userTypeReferenceIDF", 0L,
                            "mobileNumber", "",
                            "citizenId", 0L,
                            "citizenCode", ""
                    )
            );

            var auth = new UsernamePasswordAuthenticationToken(jwt, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);

            return action.get();
        } finally {
            if (previousAuth != null) {
                SecurityContextHolder.getContext().setAuthentication(previousAuth);
            } else {
                SecurityContextHolder.clearContext();
            }
        }
    }
}