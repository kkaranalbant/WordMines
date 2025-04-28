package com.kaan.WordMines.dto;

import java.util.List;
import java.util.Map;

public record GameConfirmingRequest (Long userId , Long gameId , String word , String usedLettersFromThisRound , List<List<Object>> letterAndPostionList) {
}
