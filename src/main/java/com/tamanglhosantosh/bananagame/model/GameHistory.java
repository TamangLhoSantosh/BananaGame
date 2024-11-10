package com.tamanglhosantosh.bananagame.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents the history of the game played.
 * This class stores the player's id and game status.
 * Lombok's @Data annotation generates the necessary boilerplate code
 * like getters, setters, and constructors
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "game_histories")
public class GameHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Unique identifier for each game history

    private Boolean game_status; // Game Status

    @ManyToOne
    @JoinColumn(name = "player_id", referencedColumnName = "id") // Foreign key to Player entity
    private Player player; // Reference to Player entity
}
