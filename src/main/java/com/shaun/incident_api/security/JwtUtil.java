package com.shaun.incident_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Set secret key.
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // valid 1 hour
    private final long expiration = 1000 * 60 * 60; // 1 hour in ms

    // generate JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)             // token 的主体，通常是用户名
                .setIssuedAt(new Date())          // 生成时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 过期时间
                .signWith(key)                    // 使用密钥签名
                .compact();                       // 生成字符串
    }

    // get Claims
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // get username using token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // check token if it is expired
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // check token
    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }
}
