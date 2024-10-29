package com.tamanglhosantosh.bananagame.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a player in the Banana Game application.
 * This class stores the player's details, including their username, full name,
 * email, and password.
 * Lombok's @Data annotation generates the necessary boilerplate code
 * like getters, setters, and constructors
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Unique identifier for each player

    private String username;    // Player's username
    private String firstName;   // Player's first name
    private String lastName;    // Player's last name
    private String middleName;  // Player's middle name
    private String email;       // Player's email address
    private String password;    // Player's password
}


