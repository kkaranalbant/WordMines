package com.kaan.WordMines.service;

import com.kaan.WordMines.dto.LoginRequest;
import com.kaan.WordMines.dto.RegisterRequest;
import com.kaan.WordMines.exception.UserException;
import com.kaan.WordMines.model.User;
import com.kaan.WordMines.model.UserStatus;
import com.kaan.WordMines.repo.UserRepo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User getUserById(Long userId) throws UserException {
        return userRepo.findById(userId).orElseThrow(() -> new UserException("User Not Found"));
    }

    public List<User> findAllOnlineUsers() {
        return userRepo.findAllByOnlineTrue();
    }

    public void register(RegisterRequest registerRequest) throws UserException {
        User user = userRepo.findByUsername(registerRequest.username()).get();
        if (user != null) {
            throw new UserException("User Already Exists");
        }
        String password = registerRequest.password();
        if (password.length() < 8) throw new UserException("Please Enter Valid Password");
        boolean foundUpperCase = false;
        boolean foundLowerCase = false;
        boolean foundDigit = false;
        for (char character : password.toCharArray()) {
            if (Character.isUpperCase(character)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(character)) {
                foundLowerCase = true;
            } else if (Character.isDigit(character)) {
                foundDigit = true;
            }
        }
        if (!(foundDigit && foundLowerCase && foundUpperCase)) {
            throw new UserException("Please Enter Valid Password");
        }
        user = new User();
        user.setEmail(registerRequest.email());
        user.setPassword(registerRequest.password());
        user.setUsername(registerRequest.username());
        user.setUserStatus(UserStatus.OFFLINE);
        userRepo.save(user);
    }

    public void login (LoginRequest loginRequest , HttpServletResponse response) throws UserException{
        User user = userRepo.findByUsername(loginRequest.username()).orElseThrow(() -> new UserException("User Not Found"));
        if (!user.getPassword().equals(loginRequest.password())) {
            throw new UserException("Password is not correct");
        }
        user.setUserStatus(UserStatus.ONLINE);
        Cookie cookie = new Cookie("id" , user.getId().toString());
        response.addCookie(cookie);
    }

    public void logout (HttpServletRequest request , HttpServletResponse response) {
        Cookie [] cookies = request.getCookies() ;
        Cookie idCookie = null ;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("id")) {
                idCookie = cookie ;
                break ;
            }
        }
        if (idCookie != null) {
            User user = userRepo.findById(Long.parseLong(idCookie.getValue())).orElseGet(() -> null);
            user.setUserStatus(UserStatus.OFFLINE);
            userRepo.save(user);
            Cookie cookie = new Cookie("id" , "0");
            cookie.setMaxAge(1);
            response.addCookie(cookie);
        }
    }


}
