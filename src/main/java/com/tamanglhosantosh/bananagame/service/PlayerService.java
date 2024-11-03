package com.tamanglhosantosh.bananagame.service;

import com.tamanglhosantosh.bananagame.model.Player;
import com.tamanglhosantosh.bananagame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for retrieving, writing data of player
 */
@Service
public class PlayerService {

    /**
     * Automatically injects the PlayerRepository bean
     */
    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Automatically injects the AuthenticationManager bean
     */
    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * Automatically injects the JWTService bean
     */
    @Autowired
    private JWTService jwtService;

    // Sets a BCryptPasswordEncoder with a strength of 12 for hashing passwords.
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    /**
     * Registers a new player in the system.
     *
     * @param player Player to be registered.
     * @return registered Player, or error response if player already exists.
     */
    public ResponseEntity<?> register(Player player) {
        // Check for duplicate entries
        Optional<Player> existingPlayer = playerRepository.findByUsername(player.getUsername())
                .or(() -> playerRepository.findPlayerByEmail(player.getUsername()));

        // Return null if username and email is not available
        if (existingPlayer.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username or email already exists.");
        }

        player.setPassword(passwordEncoder.encode(player.getPassword()));
        Player savedPlayer = playerRepository.save(player);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(savedPlayer); // Save and return the new player to the repository
    }

    /**
     * Authenticates a player and generates a JWT token if successful.
     *
     * @param player The Player details containing username and password.
     * @return A JWT token if authentication is successful, otherwise sends a message.
     */
    public ResponseEntity<?> login(Player player) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(player.getUsername(), player.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(player.getUsername());
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(token);
            }
        } catch (Exception e) {
            e.printStackTrace(); // To catch any errors in authentication or token generation
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password.");
    }

    /**
     * Finds a player by their ID.
     *
     * @param id The ID of the player to retrieve.
     * @return the Player, or null if not found.
     */    public Optional<Player> findById(Integer id) {
        return playerRepository.findById(id);
    }

    /**
     * Retrieves all players in the system.
     *
     * @return A list of all Player.
     */
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    /**
     * Deletes a player by their ID.
     *
     * @param id The ID of the player to delete.
     */
    public void deleteById(Integer id) {
        playerRepository.deleteById(id);
    }

}