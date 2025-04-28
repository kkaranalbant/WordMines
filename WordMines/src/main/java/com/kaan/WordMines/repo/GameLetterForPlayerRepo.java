package com.kaan.WordMines.repo;


import com.kaan.WordMines.model.GameLetterForPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameLetterForPlayerRepo extends JpaRepository<GameLetterForPlayer, Long> {

    public List<GameLetterForPlayer> findAllByGameIdAndPlayerId(Long gameId, Long playerId);


}
