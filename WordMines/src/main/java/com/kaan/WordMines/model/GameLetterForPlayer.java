package com.kaan.WordMines.model;

import jakarta.persistence.*;

@Entity
@Table(name = "game_letter_for_player")
public class GameLetterForPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private User user;

    @JoinColumn
    @ManyToOne
    private Game game;

    @ManyToOne
    @JoinColumn
    private GameLetter gameLetter;

    private boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameLetter getGameLetter() {
        return gameLetter;
    }

    public void setGameLetter(GameLetter gameLetter) {
        this.gameLetter = gameLetter;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
