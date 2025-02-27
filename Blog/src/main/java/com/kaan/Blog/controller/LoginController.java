package com.kaan.Blog.controller;

import com.kaan.Blog.dto.request.LoginRequest;
import com.kaan.Blog.model.Role;
import com.kaan.Blog.model.User;
import com.kaan.Blog.repo.UserRepo;
import com.kaan.Blog.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/login")
public class LoginController {

    private LoginService loginService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserRepo userRepo;

    public LoginController(LoginService loginService, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepo userRepo) {
        this.loginService = loginService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepo = userRepo;
    }

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest , HttpServletResponse response) {
        loginService.login(loginRequest , response);
        return ResponseEntity.ok("Welcome");
    }

    @GetMapping
    public ResponseEntity<String> register() {
        User user = new User();
        user.setId(1L);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setName("Kaan");
        user.setLastname("Karanalbant");
        user.setUsername("kkaranalbant");
        user.setBirthDate(LocalDate.of(2003, 8, 29));
        user.setPassword(bCryptPasswordEncoder.encode("asdasd"));
        user.setRole(Role.ADMIN);
        userRepo.save(user);
        return ResponseEntity.ok("Basarili");
    }

}
