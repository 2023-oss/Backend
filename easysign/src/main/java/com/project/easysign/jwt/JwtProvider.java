package com.project.easysign.jwt;

import com.project.easysign.util.DateConvertor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${jwt.secretkey}")
    private String secretKey;

    @Value("${jwt.tokenExpired}")
    private long tokenExpired;

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, username, tokenExpired);
    }
    private String generateToken(Map<String, Object> claims, String subject, Long expiryTime) {
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(expiryTime);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(DateConvertor.toDate(LocalDateTime.now()))
                .setExpiration(DateConvertor.toDate(expiryDate))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllCalims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllCalims(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public LocalDateTime extractExpiration(String token){
        return DateConvertor.toLocalDateTime(extractClaim(token, Claims::getExpiration));
    }
    public boolean validateToken(String token, String username){
        final String tokenUsername = extractUsername(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(LocalDateTime.now());
    }
    public Long getTokenExpirationDate() {
        return tokenExpired;
    }
}
