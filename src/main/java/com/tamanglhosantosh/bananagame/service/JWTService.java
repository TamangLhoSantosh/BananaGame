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

/**
 * Service for handling all the JSON Web Token (JWT) operations.
 * This class is responsible for generating, validating, and extracting
 * information from JWTs.
 */
@Service
public class JWTService {

    private final SecretKey secretKey; // Secret key used for signing the JWT

    /**
     * Constructor for JWTService.
     * Initializes the secret key used for signing JWTs.
     *
     * @throws NoSuchAlgorithmException If the specified algorithm is not available.
     */
    public JWTService() throws NoSuchAlgorithmException {
        // This code was refactored by chatgpt as the code written was not working as
        // intended

        // Use KeyGenerator to create a key with the required size for HS384
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA384");
        keyGen.init(384); // Ensures a key size of 384 bits
        this.secretKey = keyGen.generateKey();
    }

    /**
     * Generates a JWT for the given username.
     *
     * @param username The username for which to generate the token.
     * @return A signed JWT as a string.
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().claims(claims).subject(username).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 1000 * 30))
                .signWith(secretKey) // Directly sign with the generated key
                .compact();
    }

    /**
     * Retrieves the secret key used for signing JWTs.
     *
     * @return The secret key.
     */
    private SecretKey getKey() {
        return this.secretKey;
    }

    /**
     * Extracts the username from the given JWT.
     *
     * @param token The JWT from which to extract the username.
     * @return The username contained in the token.
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT.
     *
     * @param token          The JWT from which to extract the claim.
     * @param claimsResolver The function to extract the desired claim.
     * @param <T>            The type of the claim to be extracted.
     * @return The extracted claim of type T.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the given JWT.
     *
     * @param token The JWT from which to extract claims.
     * @return The claims contained in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build().parseSignedClaims(token).getPayload();
    }

    /**
     * Validates the given JWT against the provided player details.
     *
     * @param token         The JWT to validate.
     * @param playerDetails The player details to validate against.
     * @return true if the token is valid and belongs to the player; false
     *         otherwise.
     */
    public boolean validateToken(String token, UserDetails playerDetails) {
        final String username = extractUserName(token);
        return (username.equals(playerDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the given JWT has expired.
     *
     * @param token The JWT to check.
     * @return true if the token is expired; false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the given JWT.
     *
     * @param token The JWT from which to extract the expiration date.
     * @return The expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
