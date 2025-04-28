package com.kaan.WordMines.service;

import com.kaan.WordMines.model.GameLetterForPlayer;
import com.kaan.WordMines.repo.GameLetterForPlayerRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameLetterForPlayerService {

    private final GameLetterForPlayerRepo gameLetterForPlayerRepo;

    public GameLetterForPlayerService(GameLetterForPlayerRepo gameLetterForPlayerRepo) {
        this.gameLetterForPlayerRepo = gameLetterForPlayerRepo;
    }

    public List<GameLetterForPlayer> getByGameIdAndPlayerId(Long gameId, Long playerId) {
        return gameLetterForPlayerRepo.findAllByGameIdAndPlayerId(gameId, playerId);
    }

    public void add(GameLetterForPlayer gameLetterForPlayer) {
        gameLetterForPlayerRepo.save(gameLetterForPlayer);
    }

    public void saveAll(List<GameLetterForPlayer> gameLettersForPlayer) {
        gameLetterForPlayerRepo.saveAll(gameLettersForPlayer);
    }

}
