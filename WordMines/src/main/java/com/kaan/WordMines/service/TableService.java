package com.kaan.WordMines.service;

import com.kaan.WordMines.dto.AddingLetterToCellRequest;
import com.kaan.WordMines.exception.GameTableException;
import com.kaan.WordMines.model.*;
import com.kaan.WordMines.repo.GameTableRepo;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TableService {

    private final Random random;

    private final GameTableRepo gameTableRepo;

    private final GameService gameService;

    private final ExtensionService extensionService;

    private final UserService userService;

    private final GameLetterService gameLetterService ;

    public TableService(Random random, GameTableRepo gameTableRepo, GameService gameService, ExtensionService extensionService, UserService userService , GameLetterService gameLetterService) {
        this.random = random;
        this.gameTableRepo = gameTableRepo;
        this.gameService = gameService;
        this.extensionService = extensionService;
        this.userService = userService;
        this.gameLetterService = gameLetterService ;
    }

    public void initTable(GameTable gameTable) {
        GameObject[][] table = init(gameTable.getGameTable());
        gameTable.setGameTable(table);
        saveTable(gameTable);
    }

    private GameObject[][] init(GameObject[][] table) {
        table[0][2] = new Bonus(BonusType.WORD_3);
        table[0][14] = new Bonus(BonusType.WORD_3);
        table[0][5] = new Bonus(BonusType.LETTER_2);
        table[0][9] = new Bonus(BonusType.LETTER_2);
        table[1][13] = new Bonus(BonusType.LETTER_3);
        table[1][1] = new Bonus(BonusType.LETTER_3);
        table[1][6] = new Bonus(BonusType.LETTER_2);
        table[1][8] = new Bonus(BonusType.LETTER_2);
        table[2][0] = new Bonus(BonusType.WORD_3);
        table[2][14] = new Bonus(BonusType.WORD_3);
        table[2][7] = new Bonus(BonusType.WORD_2);
        table[3][3] = new Bonus(BonusType.WORD_2);
        table[3][11] = new Bonus(BonusType.WORD_2);
        table[4][4] = new Bonus(BonusType.LETTER_3);
        table[4][10] = new Bonus(BonusType.LETTER_3);
        table[5][0] = new Bonus(BonusType.LETTER_2);
        table[5][5] = new Bonus(BonusType.LETTER_2);
        table[5][9] = new Bonus(BonusType.LETTER_2);
        table[5][14] = new Bonus(BonusType.LETTER_2);
        table[6][1] = new Bonus(BonusType.LETTER_2);
        table[6][13] = new Bonus(BonusType.LETTER_2);
        table[6][6] = new Bonus(BonusType.LETTER_2);
        table[6][8] = new Bonus(BonusType.LETTER_2);
        table[7][2] = new Bonus(BonusType.WORD_2);
        table[7][12] = new Bonus(BonusType.WORD_2);
        table[7][7] = new Bonus(BonusType.JOKER);
        table[8][1] = new Bonus(BonusType.LETTER_2);
        table[8][13] = new Bonus(BonusType.LETTER_2);
        table[8][6] = new Bonus(BonusType.LETTER_2);
        table[8][8] = new Bonus(BonusType.LETTER_2);
        table[9][0] = new Bonus(BonusType.LETTER_2);
        table[9][5] = new Bonus(BonusType.LETTER_2);
        table[9][9] = new Bonus(BonusType.LETTER_2);
        table[9][14] = new Bonus(BonusType.LETTER_2);
        table[10][4] = new Bonus(BonusType.LETTER_3);
        table[10][10] = new Bonus(BonusType.LETTER_3);
        table[11][3] = new Bonus(BonusType.WORD_2);
        table[11][11] = new Bonus(BonusType.WORD_2);
        table[12][0] = new Bonus(BonusType.WORD_3);
        table[12][14] = new Bonus(BonusType.WORD_3);
        table[12][7] = new Bonus(BonusType.WORD_2);
        table[13][13] = new Bonus(BonusType.LETTER_3);
        table[13][1] = new Bonus(BonusType.LETTER_3);
        table[13][6] = new Bonus(BonusType.LETTER_2);
        table[13][8] = new Bonus(BonusType.LETTER_2);
        table[14][2] = new Bonus(BonusType.WORD_3);
        table[14][14] = new Bonus(BonusType.WORD_3);
        table[14][5] = new Bonus(BonusType.LETTER_2);
        table[14][9] = new Bonus(BonusType.LETTER_2);
        table = initObstacles(table);
        table = initRewards(table);
        return table;
    }

    private GameObject[][] initObstacles(GameObject[][] gameTable) {
        gameTable = createObstacle(gameTable, 5, ObstacleType.POINT_DIVISION);
        gameTable = createObstacle(gameTable, 4, ObstacleType.POINT_TRANSFER);
        gameTable = createObstacle(gameTable, 3, ObstacleType.LETTER_LOSE);
        gameTable = createObstacle(gameTable, 2, ObstacleType.EXTRA_MOVEMENT_OBSTACLE);
        gameTable = createObstacle(gameTable, 2, ObstacleType.WORD_CANCEL);
        return gameTable;
    }

    private GameObject[][] initRewards(GameObject[][] gameTable) {
        gameTable = createReward(gameTable, 2, RewardType.ZONE_BAN);
        gameTable = createReward(gameTable, 3, RewardType.LETTER_BAN);
        gameTable = createReward(gameTable, 2, RewardType.EXTRA_MOVEMENT);
        return gameTable;
    }

    private GameObject[][] createObstacle(GameObject[][] gameTable, int amount, ObstacleType obstacleType) {
        int counter = 0;
        while (counter != amount) {
            int row = random.nextInt(0, 15);
            int column = random.nextInt(0, 15);
            if (gameTable[row][column] == null) {
                gameTable[row][column] = new Obstacle(obstacleType);
                counter++;
            }
        }
        return gameTable;
    }

    private GameObject[][] createReward(GameObject[][] gameTable, int amount, RewardType rewardType) {
        int counter = 0;
        while (counter != amount) {
            int row = random.nextInt(0, 15);
            int column = random.nextInt(0, 15);
            if (gameTable[row][column] == null) {
                gameTable[row][column] = new Reward(rewardType);
                counter++;
            }
        }
        return gameTable;
    }

    void addLetterToCell(AddingLetterToCellRequest addingLetterToCellRequest, Long tableId) {
        GameTable gameTable = getById(tableId);
        if (isLetterAppropriateForAdding(gameTable.getGameTable(), addingLetterToCellRequest.row(), addingLetterToCellRequest.column())) {
            if (isSpecialPositionForReward(gameTable.getGameTable(), addingLetterToCellRequest.row(), addingLetterToCellRequest.column())) {
                Extensions extensions = new Extensions();
                Game game = gameService.getGameByGameTableId(tableId);
                extensions.setGame(game);
                Reward reward = (Reward) gameTable.getGameTable()[addingLetterToCellRequest.row()][addingLetterToCellRequest.column()];
                extensions.setName(reward.getRewardType().name());
                extensions.setUser(userService.getUserById(addingLetterToCellRequest.userId()));
                extensions.setActive(true);
                extensionService.addExtension(extensions);
            } else if (isSpecialPositionForBonus(gameTable.getGameTable(), addingLetterToCellRequest.row(), addingLetterToCellRequest.column())) {
                Extensions extensions = new Extensions();
                Game game = gameService.getGameByGameTableId(tableId);
                extensions.setGame(game);
                Bonus bonus = (Bonus) gameTable.getGameTable()[addingLetterToCellRequest.row()][addingLetterToCellRequest.column()];
                extensions.setName(bonus.getBonusType().name());
                extensions.setUser(userService.getUserById(addingLetterToCellRequest.userId()));
                extensions.setActive(true);
                extensionService.addExtension(extensions);
            } else if (isSpecialPositionForObstacle(gameTable.getGameTable(), addingLetterToCellRequest.row(), addingLetterToCellRequest.column())) {
                Extensions extensions = new Extensions();
                Game game = gameService.getGameByGameTableId(tableId);
                extensions.setGame(game);
                Obstacle obstacle = (Obstacle) gameTable.getGameTable()[addingLetterToCellRequest.row()][addingLetterToCellRequest.column()];
                extensions.setName(obstacle.getObstacleType().name());
                extensions.setUser(userService.getUserById(addingLetterToCellRequest.userId()));
                extensions.setActive(true);
                extensionService.addExtension(extensions);
            }
            gameTable.getGameTable()[addingLetterToCellRequest.row()][addingLetterToCellRequest.column()]
                    = gameLetterService.getGameLetterByGame(gameService.getGameByGameTableId(tableId).getId() , addingLetterToCellRequest.letter()) ;
        }
        saveTable(gameTable);
    }

    private boolean isSpecialPositionForReward(GameObject[][] table, int row, int column) {
        if (table[row][column] != null && table[row][column] instanceof Reward) return true;
        return false;
    }

    private boolean isSpecialPositionForObstacle(GameObject[][] table, int row, int column) {
        if (table[row][column] != null && table[row][column] instanceof Reward) return true;
        return false;
    }

    private boolean isSpecialPositionForBonus(GameObject[][] table, int row, int column) {
        if (table[row][column] != null && table[row][column] instanceof Reward) return true;
        return false;
    }

    private boolean isLetterAppropriateForAdding(GameObject[][] table, int row, int column) {
        if (table[row][column] != null && !(table[row][column] instanceof GameLetter)) return false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (row + i < 0 || column + j < 0) continue;
                if (table[row + i][column + j] != null) return true;
            }
        }
        return false;
    }

    public GameTable getById(Long tableId) throws GameTableException {
        return gameTableRepo.findById(tableId).orElseThrow(() -> new GameTableException("Table Not Found"));
    }

    public void saveTable(GameTable gameTable) {
        gameTableRepo.save(gameTable);
    }

}
