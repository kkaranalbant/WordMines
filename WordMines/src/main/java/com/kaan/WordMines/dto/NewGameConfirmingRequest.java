package com.kaan.WordMines.dto;

import com.kaan.WordMines.model.GameMode;

public record NewGameConfirmingRequest (Long approvalId , Long approvedId , GameMode gameMode) {
}
