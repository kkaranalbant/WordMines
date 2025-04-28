package com.kaan.WordMines.service;

import com.kaan.WordMines.model.Game;
import com.kaan.WordMines.model.GameLetter;
import com.kaan.WordMines.repo.GameLetterRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameLetterService {

    private final GameLetterRepo gameLetterRepo;

    public GameLetterService(GameLetterRepo gameLetterRepo) {
        this.gameLetterRepo = gameLetterRepo;
    }

    public void initLetters(Game game) {
        gameLetterRepo.save(new GameLetter('A', 12, 1, game));
        gameLetterRepo.save(new GameLetter('B', 2, 3, game));
        gameLetterRepo.save(new GameLetter('C', 2, 4, game));
        gameLetterRepo.save(new GameLetter('Ç', 2, 4, game));
        gameLetterRepo.save(new GameLetter('D', 2, 3, game));
        gameLetterRepo.save(new GameLetter('E', 8, 1, game));
        gameLetterRepo.save(new GameLetter('F', 1, 7, game));
        gameLetterRepo.save(new GameLetter('Ğ', 1, 8, game));
        gameLetterRepo.save(new GameLetter('H', 1, 5, game));
        gameLetterRepo.save(new GameLetter('I', 4, 2, game));
        gameLetterRepo.save(new GameLetter('İ', 7, 1, game));
        gameLetterRepo.save(new GameLetter('J', 1, 10, game));
        gameLetterRepo.save(new GameLetter('K', 7, 1, game));
        gameLetterRepo.save(new GameLetter('L', 7, 1, game));
        gameLetterRepo.save(new GameLetter('M', 4, 2, game));
        gameLetterRepo.save(new GameLetter('N', 5, 1, game));
        gameLetterRepo.save(new GameLetter('O', 3, 2, game));
        gameLetterRepo.save(new GameLetter('Ö', 1, 7, game));
        gameLetterRepo.save(new GameLetter('P', 1, 5, game));
        gameLetterRepo.save(new GameLetter('R', 6, 1, game));
        gameLetterRepo.save(new GameLetter('S', 3, 2, game));
        gameLetterRepo.save(new GameLetter('Ş', 2, 4, game));
        gameLetterRepo.save(new GameLetter('T', 5, 1, game));
        gameLetterRepo.save(new GameLetter('U', 3, 2, game));
        gameLetterRepo.save(new GameLetter('Ü', 2, 3, game));
        gameLetterRepo.save(new GameLetter('V', 1, 7, game));
        gameLetterRepo.save(new GameLetter('Y', 2, 3, game));
        gameLetterRepo.save(new GameLetter('Z', 2, 4, game));
        gameLetterRepo.save(new GameLetter('j', 2, 0, game));
    }

    public GameLetter getGameLetterByGame(Long gameId, char letter) {
        return gameLetterRepo.findAllByGameId(gameId).stream().filter((gameLetter) -> gameLetter.getLetter() == letter).toList().getFirst();
    }

    public void updateGameLetter(GameLetter gameLetter) {
        gameLetterRepo.save(gameLetter);
    }

    public List<GameLetter> getAllByGameId(Long gameId) {
        return gameLetterRepo.findAllByGameId(gameId);
    }

}
