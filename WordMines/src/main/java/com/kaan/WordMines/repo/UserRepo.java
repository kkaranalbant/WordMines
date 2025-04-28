package com.kaan.WordMines.repo;

import com.kaan.WordMines.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    public List<User> findAllByOnlineTrue();

    public Optional<User> findByUsername (String username) ;

}
