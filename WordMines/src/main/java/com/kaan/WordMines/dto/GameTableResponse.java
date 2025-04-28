package com.kaan.WordMines.dto;

import java.util.List;

public class GameTableResponse {


    private Long id;
    private List<List<GameObjectResponse>> gameObjects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<List<GameObjectResponse>> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(List<List<GameObjectResponse>> gameObjects) {
        this.gameObjects = gameObjects;
    }
}
