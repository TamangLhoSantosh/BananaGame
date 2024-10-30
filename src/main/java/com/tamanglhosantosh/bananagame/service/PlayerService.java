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

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public Player register(Player player) {
        // Check for duplicate entries
        Optional<Player> existingPlayer = playerRepository.findByUsername(player.getUsername())
                .or(() -> playerRepository.findPlayerByEmail(player.getUsername()));

        // Return null if username and email is not available
        if (existingPlayer.isPresent()) {
            return null;
        }

        player.setPassword(passwordEncoder.encode(player.getPassword()));
        return playerRepository.save(player); // Save the new player to the repository
    }

    public String login(Player player) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(player.getUsername(), player.getPassword()));

        if (authentication.isAuthenticated()) {
            return "Success";
        }
        return "Fail";
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