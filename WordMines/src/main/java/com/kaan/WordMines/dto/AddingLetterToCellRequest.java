package com.kaan.WordMines.dto;

public record AddingLetterToCellRequest(long userId , char letter, int row, int column) {
}
