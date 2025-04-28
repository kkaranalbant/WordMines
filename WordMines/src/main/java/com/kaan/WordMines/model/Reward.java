package com.kaan.WordMines.model;

public class Reward extends GameObject{

    private RewardType rewardType ;

    public Reward(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public RewardType getRewardType() {
        return rewardType;
    }
}
