package com.kaan.WordMines.mapper;

import com.kaan.WordMines.dto.GameObjectResponse;
import com.kaan.WordMines.dto.GameTableResponse;
import com.kaan.WordMines.model.*;

import java.util.ArrayList;
import java.util.List;

public class GameTableMapper {

    public static GameTableResponse toResponse(GameTable gameTable) {
        GameTableResponse response = new GameTableResponse();
        response.setId(gameTable.getId());

        GameObject[][] table = gameTable.getGameTable();
        List<List<GameObjectResponse>> rows = new ArrayList<>();

        for (GameObject[] row : table) {
            List<GameObjectResponse> rowList = new ArrayList<>();
            for (GameObject obj : row) {
                if (obj == null) {
                    rowList.add(null);
                    continue;
                }
                GameObjectResponse objResponse = new GameObjectResponse();
                objResponse.setRow(obj.getRow());
                objResponse.setColumn(obj.getColumn());
                objResponse.setClicked(obj.isClicked());

                if (obj instanceof GameLetter letter) {
                    objResponse.setType("GameLetter");
                    objResponse.setLetter(letter.getLetter());
                    objResponse.setAmount(letter.getAmount());
                    objResponse.setPoint(letter.getPoint());
                } else if (obj instanceof Obstacle obstacle) {
                    objResponse.setType("Obstacle");
                    objResponse.setObstacleType(obstacle.getObstacleType().name());
                } else if (obj instanceof Reward reward) {
                    objResponse.setType("Reward");
                    objResponse.setRewardType(reward.getRewardType().name());
                } else if (obj instanceof Bonus bonus) {
                    objResponse.setType("Bonus");
                    objResponse.setBonusType(bonus.getBonusType().name());
                } else {
                    throw new IllegalStateException("Unknown GameObject subclass: " + obj.getClass());
                }

                rowList.add(objResponse);
            }
            rows.add(rowList);
        }

        response.setGameObjects(rows);
        return response;
    }


}
