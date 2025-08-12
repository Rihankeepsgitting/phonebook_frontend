package com.phonedirectory.springboot.phonebook.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {

    @Value("${security.enabled:true}")
    private boolean securityEnabled;

    @Value("${jwt.secret}")
    private String secret; // must be base64-encoded string in properties

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private final long expirationTime = 1000L * 60 * 60 * 10; // 10 hours

    public String generateTokenWithClaims(String username, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        if (!securityEnabled) {
            return "";
        }
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRoles(String token) {
        if (!securityEnabled) {
            return "";
        }
        Claims claims = extractAllClaims(token);
        // return as string (can be null)
        Object val = claims.get("xroles");
        return val != null ? String.valueOf(val) : null;
    }

    public boolean validateToken(String token) {
        if (!securityEnabled) {
            return true;
        }
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        if (!securityEnabled) {
            return Jwts.claims();
        }
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }
}
