package com.easy.easystays.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtil {
    @Value("${jwt.privateKey}")
    private String privateKey;

    // subject == username(as we need to subtract username from jwt)
    public String generateToken(String subject) {
        return Jwts.builder()
                .setClaims(new HashMap<>()) // stores optional info in a map
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, privateKey)
                .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    public Boolean validateToken(String token) {
        return extractExpiration(token).after(new Date());
    }
}
