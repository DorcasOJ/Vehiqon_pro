package com.vehiqon.security.jwt;

import com.vehiqon.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties properties;
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(properties.secret().getBytes());
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.expiration());
        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
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

    public Boolean isExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Boolean isTokenValid(String token, String email) {
        return email.equals(extractUsername(token)) && !isExpired(token);
    }
}
