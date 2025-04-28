package com.kaan.WordMines.repo;

import com.kaan.WordMines.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepo extends JpaRepository<Game, Long> {

    public Optional<Game> findByGameTableId (Long gameTableId) ;
}
