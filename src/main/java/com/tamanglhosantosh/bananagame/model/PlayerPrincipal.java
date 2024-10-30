package com.tamanglhosantosh.bananagame.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents the authenticated player's principal information.
 * This class implements the UserDetails interface provided by Spring Security,
 * allowing the Player entity to be used for authentication and authorization.
 */
public class PlayerPrincipal implements UserDetails {

    private final Player player;

    /**
     * Constructor for PlayerPrincipal.
     *
     * @param player The player that is being authenticated.
     */
    public PlayerPrincipal(Player player) {
        this.player = player;
    }


    /**
     * Return the authority
     *
     * @return role or authority of a player.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    /**
     * Returns the player's password.
     *
     * @return The password of the player.
     */
    @Override
    public String getPassword() {
        return player.getPassword();
    }

    /**
     * Returns the player's username.
     *
     * @return The username of the player.
     */
    @Override
    public String getUsername() {
        return player.getUsername();
    }

    /**
     * Indicates whether the account is non-expired.
     *
     * @return true if the account is non-expired; false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the account is non-locked.
     *
     * @return true if the account is non-locked; false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the credentials are non-expired.
     *
     * @return true if the credentials are non-expired; false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the player is enabled.
     *
     * @return true if the player is enabled; false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
