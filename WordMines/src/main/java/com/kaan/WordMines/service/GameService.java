package com.kaan.WordMines.service;

import com.kaan.WordMines.dto.AddingLetterToCellRequest;
import com.kaan.WordMines.dto.GameConfirmingRequest;
import com.kaan.WordMines.dto.NewGameConfirmingRequest;
import com.kaan.WordMines.dto.SearchingNewGameRequest;
import com.kaan.WordMines.exception.GameException;
import com.kaan.WordMines.model.*;
import com.kaan.WordMines.repo.GameRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GameService {

    private final GameRepo gameRepo;

    private final TableService tableService;

    private final WordControlService wordControlService;

    private final Random random;

    private final UserService userService;

    private final GameLetterService gameLetterService;

    private final GameLetterForPlayerService gameLetterForPlayerService;

    public GameService(GameRepo gameRepo, TableService tableService, WordControlService wordControlService, Random random, UserService userService, GameLetterService gameLetterService, GameLetterForPlayerService gameLetterForPlayerService) {
        this.gameRepo = gameRepo;
        this.tableService = tableService;
        this.wordControlService = wordControlService;
        this.random = random;
        this.userService = userService;
        this.gameLetterService = gameLetterService;
        this.gameLetterForPlayerService = gameLetterForPlayerService;
    }

    public Game getById(Long id) throws GameException {
        return gameRepo.findById(id).orElseThrow(() -> new GameException("Game Not Found"));
    }

    public Game getGameByGameTableId(Long gameTableId) throws GameException {
        return gameRepo.findByGameTableId(gameTableId).orElseThrow(() -> new GameException("Game Not Found"));
    }


    //kabul etme isleminden sonra hem kontrol yapilir . Kullanilan kelimeler pasif hale getirilir . Yeni harfler havuzdan kullaniciya verilir
    public void confirmChanges(GameConfirmingRequest gameConfirmingRequest) {
        Game game = getById(gameConfirmingRequest.gameId());
        if (wordControlService.verify(gameConfirmingRequest.word())) {
            int score = 0;
            for (char character : gameConfirmingRequest.word().toCharArray()) {
                GameLetter gameLetter = gameLetterService.getGameLetterByGame(gameConfirmingRequest.gameId(), character);
                score += gameLetter.getPoint();
                gameLetter.setAmount(gameLetter.getAmount() - 1);
                gameLetterService.updateGameLetter(gameLetter);
            }
            if (game.getUser1().getId().longValue() == gameConfirmingRequest.userId().longValue()) {
                game.setUser1Score(score);
            } else {
                game.setUser2Score(score);
            }
        }
        List<GameLetterForPlayer> gameLettersForPlayer = gameLetterForPlayerService.getByGameIdAndPlayerId(gameConfirmingRequest.gameId(), gameConfirmingRequest.userId());
        for (int i = 0; i < gameConfirmingRequest.usedLettersFromThisRound().length(); i++) {
            for (GameLetterForPlayer gameLetterForPlayer : gameLettersForPlayer) {
                if (gameLetterForPlayer.getGameLetter().getLetter() == gameConfirmingRequest.usedLettersFromThisRound().charAt(i)) {
                    gameLetterForPlayer.setActive(false);
                }
            }
        }
        gameLetterForPlayerService.saveAll(gameLettersForPlayer);
        addRandomLettersFromPoolByGameIdAndPlayerId(gameConfirmingRequest.usedLettersFromThisRound().length(), gameConfirmingRequest.gameId(), gameConfirmingRequest.userId());
        game.setTourNumber(game.getTourNumber() + 1);
        if (game.getCurrentUser().getId().longValue() == game.getUser1().getId().longValue()) {
            game.setCurrentUser(game.getUser2());
            game.setUser1HasPassedLastRound(false);
        } else {
            game.setCurrentUser(game.getUser1());
            game.setUser2HasPassedLastRound(false);
        }
        for (List<Object> objects : gameConfirmingRequest.letterAndPostionList()) {
            char letter = (char) objects.get(0);
            int row = (int) objects.get(1);
            int column = (int) objects.get(2);
            tableService.addLetterToCell(new AddingLetterToCellRequest(gameConfirmingRequest.userId(), letter, row, column), game.getGameTable().getId());
        }
        gameRepo.save(game);
    }

    // devami gelecek.(kullaniciya bildirim gitmesi gerekli .)
    public void searchNewGame(SearchingNewGameRequest newGameStartingRequest) {
        List<User> onlineUsers = userService.findAllOnlineUsers();
        Long randomUserId = 0L;
        while (randomUserId.longValue() == 0L || randomUserId.longValue() == newGameStartingRequest.userId()) {
            randomUserId = random.nextLong(1, onlineUsers.size());
        }
    }


    // kullaniciya gelen bildirimden sonra onayladiktan sonra oyunun olusmasi icin
    public void confirmNewGameRequest(NewGameConfirmingRequest newGameConfirmingRequest) {
        Game game = new Game();
        game.setGameMode(newGameConfirmingRequest.gameMode());
        game.setGameStatus(GameStatus.ACTIVE);
        GameTable gameTable = new GameTable();
        tableService.initTable(gameTable);
        tableService.saveTable(gameTable);
        game.setGameTable(gameTable);
        game.setStartingTimeInMs(System.currentTimeMillis());
        game.setUser1(userService.getUserById(newGameConfirmingRequest.approvedId()));
        game.setUser2(userService.getUserById(newGameConfirmingRequest.approvalId()));
        game.setTourNumber(0);
        game.setUser1Score(0);
        game.setUser2Score(0);
        boolean isStarterApproval = random.nextBoolean();
        if (isStarterApproval) {
            game.setStarter(game.getUser2());
            game.setCurrentUser(game.getUser2());
        } else {
            game.setStarter(game.getUser1());
            game.setCurrentUser(game.getUser1());
        }
        game = gameRepo.save(game);
        gameLetterService.initLetters(game);
        addRandomLettersForFirstRoundByGameId(game.getId());
    }

    public void addRandomLettersForFirstRoundByGameId(Long gameId) {
        List<GameLetter> gameLetters = gameLetterService.getAllByGameId(gameId);
        while (gameLetters.size() < 7) {
            int randomIndex = random.nextInt(gameLetters.size());
            GameLetter gameLetter = gameLetters.get(randomIndex);
            if (gameLetter.getAmount() > 0) {
                gameLetter.setAmount(gameLetter.getAmount() - 1);
                gameLetterService.updateGameLetter(gameLetter);
                GameLetterForPlayer gameLetterForPlayer = new GameLetterForPlayer();
                gameLetterForPlayer.setGame(getById(gameId));
                gameLetterForPlayer.setGameLetter(gameLetter);
                gameLetterForPlayer.setUser(getById(gameId).getUser1());
                gameLetterForPlayer.setActive(true);
                gameLetterForPlayerService.add(gameLetterForPlayer);
            }
        }
        while (gameLetters.size() < 7) {
            int randomIndex = random.nextInt(gameLetters.size());
            GameLetter gameLetter = gameLetters.get(randomIndex);
            if (gameLetter.getAmount() > 0) {
                gameLetter.setAmount(gameLetter.getAmount() - 1);
                gameLetterService.updateGameLetter(gameLetter);
                GameLetterForPlayer gameLetterForPlayer = new GameLetterForPlayer();
                gameLetterForPlayer.setGame(getById(gameId));
                gameLetterForPlayer.setGameLetter(gameLetter);
                gameLetterForPlayer.setUser(getById(gameId).getUser2());
                gameLetterForPlayer.setActive(true);
                gameLetterForPlayerService.add(gameLetterForPlayer);
            }
        }
    }

    private void addRandomLettersFromPoolByGameIdAndPlayerId(int amount, Long gameId, Long playerId) throws GameException {
        List<GameLetter> gameLetters = gameLetterService.getAllByGameId(gameId);
        int sum = 0;
        for (GameLetter gameLetter : gameLetters) {
            sum += gameLetter.getAmount();
        }
        if (sum <= 0) {
            throw new GameException("Game Letter Pool is Empty");
        }
        int counter = 0;
        Game game = getById(gameId);
        List<GameLetterForPlayer> gameLettersForPlayer = new ArrayList<>();
        if (sum <= amount) {
            while (counter <= sum) {
                int randomGameLetterIndex = random.nextInt(gameLetters.size());
                GameLetter gameLetter = gameLetters.get(randomGameLetterIndex);
                if (gameLetter.getAmount() > 0) {
                    gameLetter.setAmount(gameLetter.getAmount() - 1);
                    gameLetterService.updateGameLetter(gameLetter);
                    GameLetterForPlayer gameLetterForPlayer = new GameLetterForPlayer();
                    gameLetterForPlayer.setActive(true);
                    gameLetterForPlayer.setGameLetter(gameLetter);
                    gameLetterForPlayer.setGame(game);
                    if (game.getUser1().getId().longValue() == playerId) {
                        gameLetterForPlayer.setUser(game.getUser1());
                    } else {
                        gameLetterForPlayer.setUser(game.getUser2());
                    }
                    gameLettersForPlayer.add(gameLetterForPlayer);
                    counter++;
                }
            }
        } else {
            while (counter <= amount) {
                int randomGameLetterIndex = random.nextInt(gameLetters.size());
                GameLetter gameLetter = gameLetters.get(randomGameLetterIndex);
                if (gameLetter.getAmount() > 0) {
                    gameLetter.setAmount(gameLetter.getAmount() - 1);
                    gameLetterService.updateGameLetter(gameLetter);
                    GameLetterForPlayer gameLetterForPlayer = new GameLetterForPlayer();
                    gameLetterForPlayer.setActive(true);
                    gameLetterForPlayer.setGameLetter(gameLetter);
                    gameLetterForPlayer.setGame(game);
                    if (game.getUser1().getId().longValue() == playerId) {
                        gameLetterForPlayer.setUser(game.getUser1());
                    } else {
                        gameLetterForPlayer.setUser(game.getUser2());
                    }
                    gameLettersForPlayer.add(gameLetterForPlayer);
                    counter++;
                }
            }
        }
        gameLetterForPlayerService.saveAll(gameLettersForPlayer);
    }

    public void pass(Long gameId, Long userId) {
        Game game = getById(gameId);
        if (game.getUser1().getId().longValue() == userId.longValue()) {
            if (game.isUser1HasPassedLastRound()) {
                game.setGameStatus(GameStatus.FINISHED);
                game.setUser1Won(false);
                game.setUser2Won(true);
                game.setTie(false);
            } else {
                game.setUser1HasPassedLastRound(true);
                game.setCurrentUser(game.getUser2());
                game.setTourNumber(game.getTourNumber() + 1);
            }
        }
        if (game.getUser2().getId().longValue() == userId.longValue()) {
            if (game.isUser2HasPassedLastRound()) {
                game.setGameStatus(GameStatus.FINISHED);
                game.setUser2Won(false);
                game.setUser1Won(true);
                game.setTie(false);
            } else {
                game.setUser2HasPassedLastRound(true);
                game.setCurrentUser(game.getUser1());
                game.setTourNumber(game.getTourNumber() + 1);
            }
        }
        gameRepo.save(game);
    }

    public void surrender(Long gameId, Long userId) {
        Game game = getById(gameId);
        if (game.getUser1().getId().longValue() == userId.longValue()) {
            game.setGameStatus(GameStatus.FINISHED);
            game.setUser1Won(false);
            game.setUser2Won(true);
            game.setTie(false);
        }
        if (game.getUser2().getId().longValue() == userId.longValue()) {
            game.setGameStatus(GameStatus.FINISHED);
            game.setUser2Won(false);
            game.setUser1Won(true);
            game.setTie(false);
        }
        gameRepo.save(game);
    }

}
