package com.tamanglhosantosh.bananagame.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private final SecretKey secretKey;

    public JWTService() throws NoSuchAlgorithmException {
        // Use KeyGenerator to create a key with the required size for HS384
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA384");
        keyGen.init(384); // Ensures a key size of 384 bits
        this.secretKey = keyGen.generateKey();
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().claims(claims).subject(username).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + 60 * 1000 * 30))
                .signWith(secretKey) // Directly sign with the generated key
                .compact();
    }

    private SecretKey getKey() {
        return this.secretKey;
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build().parseSignedClaims(token).getPayload();
    }

    public boolean validateToken(String token, UserDetails playerDetails) {
        final String username = extractUserName(token);
        return (username.equals(playerDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
