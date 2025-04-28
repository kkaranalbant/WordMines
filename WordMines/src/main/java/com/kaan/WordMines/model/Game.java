package com.kaan.WordMines.model;

import jakarta.persistence.*;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private User user1;

    @JoinColumn
    @ManyToOne
    private User user2;

    @JoinColumn
    @ManyToOne
    private User starter;

    @JoinColumn
    @ManyToOne
    private User currentUser;

    @Enumerated(EnumType.STRING)
    private GameMode gameMode;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @JoinColumn
    @OneToOne
    private GameTable gameTable;

    private Long startingTimeInMs;

    private int tourNumber;

    private int user1Score;

    private int user2Score;

    private boolean isUser1HasPassedLastRound;

    private boolean isUser2HasPassedLastRound;

    private boolean isUser1Won;

    private boolean isUser2Won;

    private boolean isTie;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public User getStarter() {
        return starter;
    }

    public void setStarter(User starter) {
        this.starter = starter;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameTable getGameTable() {
        return gameTable;
    }

    public void setGameTable(GameTable gameTable) {
        this.gameTable = gameTable;
    }

    public Long getStartingTimeInMs() {
        return startingTimeInMs;
    }

    public void setStartingTimeInMs(Long startingTimeInMs) {
        this.startingTimeInMs = startingTimeInMs;
    }

    public int getTourNumber() {
        return tourNumber;
    }

    public void setTourNumber(int tourNumber) {
        this.tourNumber = tourNumber;
    }

    public int getUser1Score() {
        return user1Score;
    }

    public void setUser1Score(int user1Score) {
        this.user1Score = user1Score;
    }

    public int getUser2Score() {
        return user2Score;
    }

    public void setUser2Score(int user2Score) {
        this.user2Score = user2Score;
    }

    public boolean isUser1HasPassedLastRound() {
        return isUser1HasPassedLastRound;
    }

    public void setUser1HasPassedLastRound(boolean user1HasPassedLastRound) {
        isUser1HasPassedLastRound = user1HasPassedLastRound;
    }

    public boolean isUser2HasPassedLastRound() {
        return isUser2HasPassedLastRound;
    }

    public void setUser2HasPassedLastRound(boolean user2HasPassedLastRound) {
        isUser2HasPassedLastRound = user2HasPassedLastRound;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isUser1Won() {
        return isUser1Won;
    }

    public void setUser1Won(boolean user1Won) {
        isUser1Won = user1Won;
    }

    public boolean isUser2Won() {
        return isUser2Won;
    }

    public void setUser2Won(boolean user2Won) {
        isUser2Won = user2Won;
    }

    public boolean isTie() {
        return isTie;
    }

    public void setTie(boolean tie) {
        isTie = tie;
    }
}
