package com.kaan.WordMines.repo;

import com.kaan.WordMines.model.GameLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameLetterRepo extends JpaRepository<GameLetter, Long> {

    public List<GameLetter> findAllByGameId (Long gameId) ;

}
