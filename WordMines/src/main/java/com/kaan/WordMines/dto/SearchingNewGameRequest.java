package com.kaan.WordMines.dto;

import com.kaan.WordMines.model.GameMode;

public record SearchingNewGameRequest(Long userId , GameMode gameMode) {
}
