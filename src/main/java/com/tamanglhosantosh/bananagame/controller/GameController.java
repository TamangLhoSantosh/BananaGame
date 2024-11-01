package com.tamanglhosantosh.bananagame.controller;

import com.tamanglhosantosh.bananagame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling game-related API requests.
 * This class is responsible for handling requests related to the game.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class GameController {

    /**
     * Service for handling game-related operations.
     */
    @Autowired
    private GameService gameService;

    /**
     * Retrieves the game details.
     *
     * @return The game details.
     */
    @GetMapping("/game")
    public ResponseEntity<?> getGame() {
        return gameService.getGame();
    }
}
