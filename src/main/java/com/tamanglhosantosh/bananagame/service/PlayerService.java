package com.tamanglhosantosh.bananagame.service;

import com.tamanglhosantosh.bananagame.model.Player;
import com.tamanglhosantosh.bananagame.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for managing player data. It is used for retrieving,
 * writing data of player.
 */
@Service
public class PlayerService {

    /**
     * PlayerRepository instance used for interacting with the player data in the database.
     * It handles CRUD operations related to Player entities, such as creating, updating,
     * and retrieving player records.
     */
    private final PlayerRepository playerRepository;

    /**
     * AuthenticationManager instance for handling authentication logic.
     * It is used to authenticate users, ensuring that only authorized players can access certain services.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * JWTService instance used to handle JWT token creation and validation.
     * This service is responsible for issuing JWT tokens and verifying their authenticity during
     * authentication and authorization processes.
     */
    private final JWTService jwtService;

    /**
     * BCryptPasswordEncoder instance for securely hashing player passwords.
     * This encoder is used to hash passwords before saving them to the database, ensuring
     * secure storage of player credentials.
     */
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor for the PlayerService class, which initializes it with the necessary dependencies.
     * This constructor sets up the service with components for handling player data, authentication,
     * password security, and JWT management.
     *
     * @param playerRepository The repository used for managing Player entities.
     * @param jwtService The service used for creating and verifying JWT tokens.
     * @param authenticationManager The manager for authenticating players.
     * @param passwordEncoder The encoder for securely hashing player passwords.
     */
    public PlayerService(PlayerRepository playerRepository, JWTService jwtService,
                         AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new player in the system.
     *
     * @param player Player to be registered.
     * @return registered Player, or error response if player already exists.
     */
    public ResponseEntity<?> register(Player player) {
        // Check for duplicate entries for username or email
        if (playerRepository.findByUsername(player.getUsername()).isPresent() ||
                playerRepository.findPlayerByEmail(player.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username or email already exists.");
        }

        player.setPassword(passwordEncoder.encode(player.getPassword()));
        Player savedPlayer = playerRepository.save(player);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPlayer); // Save and return the new player to the repository
    }
    /**
     * Authenticates a player and generates a JWT token if successful.
     *
     * @param player The Player details containing username and password.
     * @return A JWT token if authentication is successful, otherwise sends an error message.
     */
    public ResponseEntity<?> login(Player player) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(player.getUsername(), player.getPassword()));

            if (!authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password.");
            }

            Optional<Player> loggedPlayer = playerRepository.findByUsername(player.getUsername());
            if (loggedPlayer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password.");
            }

            String token = jwtService.generateToken(player.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("id", loggedPlayer.get().getId());

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password.");
        }
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