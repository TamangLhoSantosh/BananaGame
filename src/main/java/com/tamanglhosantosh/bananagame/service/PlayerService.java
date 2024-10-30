package com.tamanglhosantosh.bananagame.service;

import com.tamanglhosantosh.bananagame.model.Player;
import com.tamanglhosantosh.bananagame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @return registered Player, or null if username/email is already in use.
     */
    public Player register(Player player) {
        // Check for duplicate entries
        Optional<Player> existingPlayer = playerRepository.findByUsername(player.getUsername())
                .or(() -> playerRepository.findPlayerByEmail(player.getUsername()));

        // Return null if username and email is not available
        if (existingPlayer.isPresent()) {
            return null;
        }

        player.setPassword(passwordEncoder.encode(player.getPassword()));
        return playerRepository.save(player); // Save and return the new player to the repository
    }

    /**
     * Authenticates a player and generates a JWT token if successful.
     *
     * @param player The Player details containing username and password.
     * @return A JWT token if authentication is successful, otherwise "Fail".
     */
    public String login(Player player) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(player.getUsername(), player.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(player.getUsername());
        }
        return "Fail";
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