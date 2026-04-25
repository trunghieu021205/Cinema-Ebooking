package com.cinemaebooking.backend.user.infrastructure.adapter;

import com.cinemaebooking.backend.user.application.port.JwtProvider;
import com.cinemaebooking.backend.user.domain.valueObject.UserId;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProviderImpl implements JwtProvider {

    // ⚠️ nên đưa vào application.yml sau này
    private static final String SECRET = "my-secret-key-my-secret-key-my-secret-key-123456";

    private static final long EXPIRATION = 86400000; // 1 day

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(UserId userId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId.getValue()))
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateResetToken(UserId userId) {

        long RESET_EXPIRATION = 15 * 60 * 1000; // 15 minutes

        return Jwts.builder()
                .setSubject(String.valueOf(userId.getValue()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + RESET_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public UserId extractUserId(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return new UserId(Long.parseLong(subject));
    }
}