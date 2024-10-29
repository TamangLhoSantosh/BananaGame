package com.tamanglhosantosh.bananagame.controller;

import com.tamanglhosantosh.bananagame.dao.PlayerRepository;
import com.tamanglhosantosh.bananagame.dto.LoginRequest;
import com.tamanglhosantosh.bananagame.model.Player;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing Player entities in the Banana Game.
 * This class provides endpoints like retrieving, creating, etc.
 * for Player data.
 */
@RestController
@RequestMapping("/api")
public class PlayerController {
    private final PlayerRepository playerService; // Service for interacting with Player data

    /**
     * Constructor for PlayerController.
     *
     * @param playerService The repository used to manage Player entities.
     */
    public PlayerController(PlayerRepository playerService) {
        this.playerService = playerService;
    }

    /**
     * Retrieves all players in the system.
     *
     * @return A list of all Player entities.
     */
    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return playerService.findAll(); // Fetch all players from the repository
    }

    /**
     * Retrieves a specific player by their ID.
     *
     * @param id The ID of the player to retrieve.
     * @return The Player entity with the specified ID, or null if not found.
     */
    @GetMapping("/player")
    public Player getPlayerById(@RequestParam("id") Integer id) {
        Optional<Player> player = playerService.findById(id); // Look for the player by ID
        return player.orElse(null); // Return the player if found, otherwise return null
    }

    /**
     * Check if username and email is available.
     * Creates a new player in the system.
     *
     * @param player Player entity to create.
     * @return The created Player entity if username and
     * email are available, otherwise null.
     */
    @PostMapping("/player")
    public Player createPlayer(@RequestBody Player player) {
        // Check for duplicate entries
        Optional<Player> existingPlayer = playerService.findByUsername(player.getUsername())
                .or(() -> playerService.findPlayerByEmail(player.getUsername()));

        // Return null if username and email is not available
        if (existingPlayer.isPresent()) {
            return null;
        }
        return playerService.save(player); // Save the new player to the repository
    }

    /**
     * Deletes existing player data in the system.
     *
     * @param id The id of the player to delete.
     */
    @DeleteMapping("/player")
    public void deletePlayer(@RequestParam("id") Integer id) {
        Optional<Player> player = playerService.findById(id);
        player.ifPresent(playerService::delete);
    }

    /**
     * Logs in a player by checking their credentials.
     *
     * @param loginRequest The login request containing username and password.
     * @return The Player entity if credentials are valid, otherwise null.
     */
    @PostMapping("/login")
    public Player login(@RequestBody LoginRequest loginRequest) {
        // Attempt to fetch the player by username or email
        Optional<Player> player = playerService.findByUsername(loginRequest.getUsername())
                .or(() -> playerService.findPlayerByEmail(loginRequest.getUsername()));

        // Validate the player's presence and password
        return player.filter(p -> loginRequest.getPassword().equals(p.getPassword()))
                .orElse(null);
    }
}
