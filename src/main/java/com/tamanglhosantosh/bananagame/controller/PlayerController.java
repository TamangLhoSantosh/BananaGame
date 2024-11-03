package com.tamanglhosantosh.bananagame.controller;

import com.tamanglhosantosh.bananagame.service.PlayerService;
import com.tamanglhosantosh.bananagame.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing Player entities in the Banana Game.
 * This class provides endpoints like retrieving, creating, etc.
 * for Player data.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class PlayerController {

    /**
     * Service for interacting with Player data
     */
    private final PlayerService playerService;

    /**
     * Constructor for PlayerController.
     *
     * @param playerService The repository used to manage Player entities.
     */
    public PlayerController(PlayerService playerService) {
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
        return playerService.findById(id).orElse(null); // Fetch player by ID from the repository
    }

    /**
     * Check if username and email is available.
     * Creates a new player in the system.
     *
     * @param player Player entity to create.
     * @return The created Player entity if username and
     *         email are available, otherwise null.
     */
    @PostMapping("/player/register")
    public ResponseEntity<?> register(@RequestBody Player player) {
        return playerService.register(player); // Adds new player to the database
    }

    /**
     * Deletes existing player data in the system.
     *
     * @param id The id of the player to delete.
     */
    @DeleteMapping("/player")
    public void deletePlayer(@RequestParam("id") Integer id) {
        playerService.deleteById(id); // Deletes the data of the selected player
    }

    /**
     * w
     * Logs in a player by checking their credentials.
     *
     * @param player The player request body containing username and password.
     * @return The Player entity if credentials are valid, otherwise null.
     */
    @PostMapping("/player/login")
    public ResponseEntity<?> login(@RequestBody Player player) {
        return playerService.login(player);
    }
}
