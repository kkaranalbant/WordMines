package com.kaan.WordMines.repo;

import com.kaan.WordMines.model.GameTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameTableRepo extends JpaRepository<GameTable , Long> {
}
