package com.tamanglhosantosh.bananagame.repository;

import com.tamanglhosantosh.bananagame.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Player entities in the database.
 * This interface extends JpaRepository, providing built-in CRUD operations
 * and the ability to define custom query methods for Player entities.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findByUsername(String username); // Method to find player by username

    Optional<Player> findPlayerByEmail(String email); // Method to find player by email
}
