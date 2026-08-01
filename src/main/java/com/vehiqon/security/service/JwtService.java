package com.vehiqon.security.service;

import com.vehiqon.security.config.UserAgentParserService;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
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

    public String generateRefreshToken(UserEntity userEntity,  Map<String, Object> extraClaims, String jti ) {
        Date now = new Date();
        Date expiry = new Date(
                now.getTime() + properties.refreshExpiration()
        );
        return Jwts.builder()
                .id(jti)
                .subject(userEntity.getUsername())
                .claims(extraClaims) //deviceId
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
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
