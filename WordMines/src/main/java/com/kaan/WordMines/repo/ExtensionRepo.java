package com.kaan.WordMines.repo;

import com.kaan.WordMines.model.Extensions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExtensionRepo extends JpaRepository<Extensions, Long> {

    public Optional<Extensions> findByGameId(Long gameId); ;

    public List<Extensions> findAllByGameIdAndUserId (Long gameId , Long userId) ;

}
