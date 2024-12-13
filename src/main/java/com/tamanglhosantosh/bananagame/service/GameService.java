package com.tamanglhosantosh.bananagame.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamanglhosantosh.bananagame.model.GameHistory;
import com.tamanglhosantosh.bananagame.repository.GameHistoryRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for handling game-related operations.
 * This class is responsible for fetching game data from an external API.
 */
@Service
public class GameService {

    /**
     * RestTemplate instance used for making HTTP requests to external APIs or services.
     * It provides convenient methods for various HTTP operations (GET, POST, etc.)
     * and simplifies the process of sending and receiving HTTP requests/responses.
     */
    private final RestTemplate restTemplate;

    /**
     * GameHistoryRepository instance for interacting with the database.
     * This repository is automatically injected (by Spring) and used to perform CRUD operations
     * on game history records, providing a way to store and retrieve game history data.
     */
    private final GameHistoryRepository gameHistoryRepository;

    /**
     * Constructor for the GameService class, which initializes the service with
     * required dependencies. This ensures the GameService has the tools it needs to
     * make HTTP requests and manage game history records.
     *
     * @param restTemplate The RestTemplate used to handle HTTP requests in the game service.
     * @param gameHistoryRepository The repository used to manage and persist game history records.
     */
    public GameService(RestTemplate restTemplate, GameHistoryRepository gameHistoryRepository) {
        this.restTemplate = restTemplate;
        this.gameHistoryRepository = gameHistoryRepository;
    }

    /**
     * Retrieves the game details from an external API.
     *
     * @return The obtained game details.
     */
    public ResponseEntity<?> getGame() {
        try {
            String gameURL = "https://marcconrad.com/uob/banana/api.php?out=json";
            ResponseEntity<String> response = restTemplate.exchange(gameURL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {
                    });
            // Parse the response body as JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());

            // Extract question and solution
            String question = jsonResponse.path("question").asText();
            int solution = jsonResponse.path("solution").asInt();
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("question", question);
            responseMap.put("solution", solution);
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);
            } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Excepetion occurred while fetching game data");
        }
    }

    /**
     * Add history of the player
     *
     * @param gameHistory Game history entity to add
     * @return the added game history
     */
    public ResponseEntity<?> save(GameHistory gameHistory) {
        try {
            gameHistory.setPlayedAt(Timestamp.from(Instant.now()));
            GameHistory history = gameHistoryRepository.save(gameHistory);
            return ResponseEntity.status(HttpStatus.CREATED).body(history);
        }catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the game history based on player id
     * @param playerId id to search for player
     * @return the list of game history if exists, else null
     */
    public ResponseEntity<?> getGameHistory(int playerId) {
        try{
            List<GameHistory> history = gameHistoryRepository.findByPlayerId(playerId);
            return ResponseEntity.status(HttpStatus.OK).body(history);
        }
        catch (Exception e) {
            return null;
        }
    }
}
