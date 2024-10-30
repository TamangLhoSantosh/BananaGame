package com.tamanglhosantosh.bananagame.service;

import com.tamanglhosantosh.bananagame.model.Player;
import com.tamanglhosantosh.bananagame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player register(Player player) {
        // Check for duplicate entries
        Optional<Player> existingPlayer = playerRepository.findByUsername(player.getUsername())
                .or(() -> playerRepository.findPlayerByEmail(player.getUsername()));

        // Return null if username and email is not available
        if (existingPlayer.isPresent()) {
            return null;
        }
        return playerRepository.save(player); // Save the new player to the repository
    }

    public Player login(Player player) {
        // Attempt to fetch the player by username or email
        Optional<Player> existingPlayer = playerRepository.findByUsername(player.getUsername())
                .or(() -> playerRepository.findPlayerByEmail(player.getUsername()));

        // Validate the player's presence and password
        return existingPlayer.filter(p -> player.getPassword().equals(p.getPassword()))
                .orElse(null);
    }

    public Optional<Player> findById(Integer id) {
        return playerRepository.findById(id);
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public void deleteById(Integer id) {
        playerRepository.deleteById(id);
    }

}