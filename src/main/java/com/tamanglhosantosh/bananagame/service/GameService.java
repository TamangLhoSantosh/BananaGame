package com.tamanglhosantosh.bananagame.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for handling game-related operations.
 * This class is responsible for fetching game data from an external API.
 */
@Service
public class GameService {

    /**
     * RestTemplate for making HTTP requests.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Retrieves the game details from an external API.
     *
     * @return The obtained game details.
     */
    public ResponseEntity<?> getGame() {
        try {
            String gameURL = "https://marcconrad.com/uob/banana/api.php?out=json";
            ResponseEntity<String> response = restTemplate.exchange(gameURL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<String>() {
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
                    "Excpetion occurred while fetching game data");
        }
    }
}
