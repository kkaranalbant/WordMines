package com.kaan.WordMines.service;

import org.springframework.stereotype.Service;

@Service
public class TurkishWordControlImpl implements WordControlService{

    @Override
    public boolean verify(String word) {
        return false;
    }
}
