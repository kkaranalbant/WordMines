package com.kaan.WordMines.mapper;

import com.kaan.WordMines.dto.GameResponse;
import com.kaan.WordMines.model.Game;

public class GameMapper {

    public static GameResponse toResponse(Game game) {
        GameResponse gameResponse = new GameResponse();
        gameResponse.setId(game.getId());
        gameResponse.setGameTableResponse(GameTableMapper.toResponse(game.getGameTable()));
        gameResponse.setGameStatus(game.getGameStatus());
        gameResponse.setGameMode(game.getGameMode());
        gameResponse.setStarter(game.getStarter());
        gameResponse.setTie(game.isTie());
        gameResponse.setCurrentUser(game.getCurrentUser());
        gameResponse.setTourNumber(game.getTourNumber());
        gameResponse.setUser1(game.getUser1());
        gameResponse.setUser1(game.getUser2());
        gameResponse.setUser1HasPassedLastRound(game.isUser1HasPassedLastRound());
        gameResponse.setUser2HasPassedLastRound(game.isUser2HasPassedLastRound());
        gameResponse.setStartingTimeInMs(game.getStartingTimeInMs());
        gameResponse.setUser1Score(game.getUser1Score());
        gameResponse.setUser2Score(game.getUser2Score());
        gameResponse.setUser1Won(game.isUser1Won());
        gameResponse.setUser2Won(game.isUser2Won());
        return gameResponse;
    }


}
