package com.tamanglhosantosh.bananagame.controller;

import com.tamanglhosantosh.bananagame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/game")
    public ResponseEntity<?> getGame() {
        return gameService.getGame();
    }
}
