package com.kaan.WordMines.model;

public class Obstacle extends GameObject {

    private ObstacleType obstacleType;

    public Obstacle(ObstacleType obstacleType) {
        this.obstacleType = obstacleType;
    }

    public ObstacleType getObstacleType() {
        return obstacleType;
    }
}
