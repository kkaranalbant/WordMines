package com.kaan.WordMines.model;


import jakarta.persistence.*;

@Entity
@Table(name = "tables")
public class GameTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Lob()
    private GameObject[][] gameTable;


    public GameTable() {
        gameTable = new GameObject[15][15];
    }

    public GameObject[][] getGameTable() {
        return gameTable;
    }

    public void setGameTable(GameObject[][] gameTable) {
        this.gameTable = gameTable;
    }

    public Long getId() {
        return id;
    }
}
