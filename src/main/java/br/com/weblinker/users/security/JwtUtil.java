package br.com.weblinker.users.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "accessSecret";
    private static final String REFRESH_SECRET_KEY = "refreshSecret";
    private static final long ACCESS_EXPIRATION = 1000 * 60 * 15; // 15 minutes
    private static final long REFRESH_EXPIRATION = 1000 * 60 * 60 * 24; // 1 day

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token, boolean isRefreshToken) {
        String secretKey = isRefreshToken ? REFRESH_SECRET_KEY : SECRET_KEY;
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, boolean isRefreshToken) {
        try {
            String secretKey = isRefreshToken ? REFRESH_SECRET_KEY : SECRET_KEY;
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}