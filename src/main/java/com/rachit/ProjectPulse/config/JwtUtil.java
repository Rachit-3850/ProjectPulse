package com.rachit.ProjectPulse.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "THIS_IS_A_SECRET_KEY_FOR_JWT_256_BIT_KEY_PROJECTPULSE_123456"; // must be 32+ chars

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username, String role) {

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(getSigningKey())  // ✔ New API
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())   // ✔ New API
                .build()
                .parseSignedClaims(token)
                .getPayload();                // ✔ New API
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = parseClaims(token);

            String username = claims.getSubject();
            Date expiration = claims.getExpiration();

            return (username.equals(userDetails.getUsername()) && !expiration.before(new Date()));

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            System.out.println("JWT Expired: " + ex.getMessage());
        } catch (io.jsonwebtoken.SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (io.jsonwebtoken.MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (Exception ex) {
            System.out.println("Token validation error: " + ex.getMessage());
        }

        return false;
    }

}
