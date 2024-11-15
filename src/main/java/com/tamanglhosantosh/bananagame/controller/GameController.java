package com.tamanglhosantosh.bananagame.controller;

import com.tamanglhosantosh.bananagame.model.GameHistory;
import com.tamanglhosantosh.bananagame.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling game-related API requests.
 * This class is responsible for handling requests related to the game.
 */
@RestController
@RequestMapping("/api")
public class GameController {

    /**
     * Service for handling game-related operations.
     */
    private final GameService gameService;


    /**
     * Constructor for GameController.
     *
     * @param gameService The repository used to manage Player entities.
     */
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Retrieves the game details.
     *
     * @return The game details.
     */
    @GetMapping("/game")
    public ResponseEntity<?> getGame() {
        return gameService.getGame();
    }

    /**
     * Add history of user to the system.
     *
     * @param gameHistory Game history entity to add
     * @return the added game history
     */
    @PostMapping("/game-history")
    public ResponseEntity<?> addGameHistory(@RequestBody GameHistory gameHistory) {
        return gameService.save(gameHistory);
    }

    /**
     * Retrieves the game history based on player id
     * @param playerId id to search for player
     * @return the list of game history if exists, else null
     */
    @GetMapping("/game-history")
    public ResponseEntity<?> getGameHistory(@RequestParam int playerId) {
        return gameService.getGameHistory(playerId);
    }
}
