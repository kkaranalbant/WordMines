package com.kaan.WordMines.model;

public enum GameMode {

    QUICK_2(2 * 60 * 1000),
    QUICK_5(5 * 60 * 1000),
    LONG_12(12 * 60 * 60 * 1000),
    LONG_24(24 * 60 * 60 * 1000);

    private long timeInMs;

    private GameMode(long timeInMs) {
        this.timeInMs = timeInMs;
    }

    public long getTimeInMs() {
        return timeInMs;
    }
}
