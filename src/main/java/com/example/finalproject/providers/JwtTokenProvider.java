package com.example.finalproject.providers;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long accessExpirationInMs;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpirationInMs;

    private final Key signingKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        // Создание ключа для подписи JWT
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Генерация Access-токена
    public String generateToken(UUID userId, UUID roleId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());  // Преобразуем UUID в String
        claims.put("roleId", roleId.toString());
        claims.put("type", "access");
        return createToken(claims, accessExpirationInMs);
    }

    // Генерация Refresh-токена
    public String generateRefreshToken(UUID userId, UUID roleId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("roleId", roleId.toString());
        claims.put("type", "refresh");
        return createToken(claims, refreshExpirationInMs);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    // Валидация Refresh-токена
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenType = claims.get("type", String.class);
            return "refresh".equals(tokenType) && !isTokenExpired(claims);
        } catch (JwtException e) {
            return false;
        }
    }

    // Проверка истечения срока действия токена
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }


    // Получение userId из токена
    public UUID getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return UUID.fromString(claims.get("userId", String.class));  // Преобразуем String обратно в UUID
    }

    // Получение roleId из токена
    public UUID getRoleId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return UUID.fromString(claims.get("roleId", String.class));
    }

    // Получение типа токена (access или refresh)
    public String getTokenType(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("type", String.class);
    }

    // Приватный метод для создания токена
    private String createToken(Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey, SignatureAlgorithm.HS512)  // Указываем алгоритм и ключ для подписи
                .compact();
    }
}
