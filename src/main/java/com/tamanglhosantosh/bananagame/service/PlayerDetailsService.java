package com.tamanglhosantosh.bananagame.service;

import com.tamanglhosantosh.bananagame.model.Player;
import com.tamanglhosantosh.bananagame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PlayerDetailsService implements UserDetailsService {
    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Player> player = playerRepository.findByUsername(username);
        if (player.isEmpty()) {
            System.out.println(("Player not found: " + username));
            throw new UsernameNotFoundException("Player not found: " + username);
        }
        return new PlayerPrincipal(player.get());
    }
}