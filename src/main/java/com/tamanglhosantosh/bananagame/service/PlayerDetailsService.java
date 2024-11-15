package com.tamanglhosantosh.bananagame.service;

import com.tamanglhosantosh.bananagame.model.Player;
import com.tamanglhosantosh.bananagame.model.PlayerPrincipal;
import com.tamanglhosantosh.bananagame.repository.PlayerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Service for loading data in security context
 * This class implements Spring Security's UserDetailsService interface
 * to provide user authentication based on username.
 */
@Service
public class PlayerDetailsService implements UserDetailsService {

    /**
     * PlayerRepository instance for managing Player entities in the database.
     * This repository is automatically injected by Spring, enabling CRUD operations
     * for Player records, such as finding, saving, updating, and deleting player data.
     */
    private final PlayerRepository playerRepository;

    /**
     * Constructor for the PlayerDetailsService class, initializing it with the necessary
     * PlayerRepository dependency. This setup ensures that PlayerDetailsService has access
     * to player data and can perform database operations related to player management.
     *
     * @param playerRepository The repository used to handle CRUD operations for Player entities.
     */
    public PlayerDetailsService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    /**
     * Loads user details by username.
     *
     * @param username The username of the player to load.
     * @return UserDetails containing the player's information.
     * @throws UsernameNotFoundException If the player with the specified username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Player> player = playerRepository.findByUsername(username);
        if (player.isEmpty()) {
            throw new UsernameNotFoundException("Player not found: " + username);
        }
        return new PlayerPrincipal(player.get());
    }
}