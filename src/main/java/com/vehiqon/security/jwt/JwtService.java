package com.vehiqon.security.jwt;

import com.vehiqon.features.onboarding.entity.RefreshTokenEntity;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties properties;
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(properties.secret().getBytes());
    }

    public String generateToken(UserEntity userEntity,
                                Map<String, Object> extraClaims) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.expiration());
        return Jwts.builder()
                .subject(userEntity.getUsername())
                .claims(extraClaims)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(UserEntity userEntity) {
        Date now = new Date();
        Date expiry = new Date(
                now.getTime() + properties.refreshExpiration()
        );
        return Jwts.builder()
                .subject(userEntity.getUsername())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public RefreshTokenEntity getRefreshTokenToSave(String refreshToken, UserEntity user, HttpServletRequest request) {
        return RefreshTokenEntity.builder()
                .token(refreshToken)
                .user(user)
                .deviceName(request.getHeader("User-Agent"))
                .deviceId(UUID.randomUUID().toString())
                .ipAddress(request.getRemoteAddr())
                .expiresAt(
                        LocalDateTime.now()
                                .plusSeconds(
                                        properties.refreshExpiration() / 1000
                                )
                )
                .revoked(false)
                .expired(false)
                .build();

    }
    public String extractUsername(String token) {

        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }

    public boolean isTokenValid(String token, String email) {
        return email.equals(extractUsername(token)) && !isExpired(token);
    }
}
