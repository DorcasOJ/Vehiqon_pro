package com.vehiqon.security.jwt;

import com.vehiqon.common.service.UserAgentParserService;
import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.onboarding.entity.RefreshTokenEntity;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
//@ConfigurationPropertiesScan
public class JwtService {
    private final JwtProperties properties;
    private final UserAgentParserService userAgentParserService;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(properties.secret().getBytes());
    }

    public String generateToken(UserEntity userEntity,
                                Map<String, Object> extraClaims) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.expiration());
        String jti = UUID.randomUUID().toString();
        return Jwts.builder()
                .id(jti)
                .subject(userEntity.getUsername())
                .claims(extraClaims)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(UserEntity userEntity,  Map<String, Object> extraClaims ) {
        Date now = new Date();
        Date expiry = new Date(
                now.getTime() + properties.refreshExpiration()
        );
        return Jwts.builder()
                .subject(userEntity.getUsername())
                .claims(extraClaims) //sessionId
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public RefreshTokenEntity mapRefreshTokenToEntity(String refreshToken, UserEntity userEntity, HttpServletRequest request, UUID sessionId) {

        AnalyticsDto.SessionContext sessionContext = userAgentParserService.parseRequestDetails(request);
        return RefreshTokenEntity.builder()
                .token(refreshToken)
                .userId(userEntity.getId())
                .deviceName(sessionContext.device())
                .deviceId(sessionContext.deviceId())
                .ipAddress(sessionContext.ipAddress())
                .expiresAt(
                        LocalDateTime.now()
                                .plus(Duration.ofMillis(properties.refreshExpiration()))
                )
                .revoked(false)
                .expired(false)
                .build();

    }

    public  UUID extractSessionId(String token) {
        Claims claims = extractClaims(token);
        return UUID.fromString(
                claims.get("sessionId", String.class)
        );
    }

    public  String extractJti(String token) {
        return extractClaims(token).getId();
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
