package com.vehiqon.security.jwt;

import com.vehiqon.common.api.dto.RequestContext;
import com.vehiqon.features.infrastructure.redis.RedisCacheService;
import com.vehiqon.security.config.UserAgentParserService;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.security.session.entity.RefreshTokenEntity;
import com.vehiqon.security.session.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
//@ConfigurationPropertiesScan
public class JwtService {
    private final JwtProperties properties;
    private RedisCacheService redisService;
    private final UserAgentParserService userAgentParserService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RequestContext requestContext;

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

    public String generateRefreshToken(UserEntity userEntity,  Map<String, Object> extraClaims) {
        String jti = UUID.randomUUID().toString();
        String redisKey = "REFRESH_TOKEN" + userEntity.getEmail() + ":" + jti;
        redisService.set(redisKey, "VALID", Duration.ofMillis( properties.refreshExpiration()));


        Date now = new Date();
        Date expiry = new Date(
                now.getTime() + properties.refreshExpiration()
        );
        String refreshToken = Jwts.builder()
                .id(jti)
                .subject(userEntity.getUsername())
                .claims(extraClaims) //deviceId
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();


        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .tokenHash(refreshToken)
                .userId(userEntity.getId())
                .deviceName(requestContext.getDeviceName())
                .deviceId(requestContext.getDeviceId())
                .ipAddress(requestContext.getIpAddress())
                .jti(jti)
                .expiresAt(
                        LocalDateTime.now()
                                .plus(Duration.ofMillis(properties.refreshExpiration()))
                )
                .revoked(false)
                .expired(false)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
        return refreshToken;
    }

    public  UUID extractDeviceId(String token) {
        Claims claims = extractClaims(token);
        return UUID.fromString(
                claims.get("deviceId", String.class)
        );
    }

    public  String extractJti(String token) {
        return extractClaims(token).getId();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
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

    public boolean validateAccessToken(String token) {
        try {
            Claims claims = extractClaims(token);
            String jti = claims.getId();
            if(redisService.hasKey("BLACKLIST:ACCESS_TOKEN:"+ jti)) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}
