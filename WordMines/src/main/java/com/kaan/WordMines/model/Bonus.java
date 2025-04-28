package com.kaan.WordMines.model;

public class Bonus extends GameObject {

    private BonusType bonusType;

    public Bonus(BonusType bonusType) {
        super();
        this.bonusType = bonusType ;
        setClicked(true);
    }

    public BonusType getBonusType() {
        return bonusType;
    }
}
