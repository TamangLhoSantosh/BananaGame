package com.tamanglhosantosh.bananagame.repository;

import com.tamanglhosantosh.bananagame.model.GameHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing game history entities in the database.
 * This interface extends JpaRepository, providing built-in CRUD operations
 * and the ability to define custom query methods for Player entities.
 */
@Repository
public interface GameHistoryRepository extends JpaRepository<GameHistory, Integer> {
    List<GameHistory> findByPlayerId(int playerId);
}
