package com.kaan.Blog.controller;

import com.kaan.Blog.dto.request.user.UserUpdatingRequest;
import com.kaan.Blog.dto.response.user.UserResponse;
import com.kaan.Blog.service.JwtService;
import com.kaan.Blog.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private UserService userService;
    private JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/get")
    public ResponseEntity<UserResponse> getUser(@CookieValue("Authorization") String jwt) {
        Long id = jwtService.getUserIdByEncodedToken(jwt);
        return ResponseEntity.ok(userService.getUserByIdForFrontend(id));
    }

    @PostMapping(value = "/update" , consumes = "multipart/form-data")
    public ResponseEntity<String> updateUser(@CookieValue("Authorization") String jwt, @RequestBody UserUpdatingRequest userUpdatingRequest , @RequestPart MultipartFile image) {
        Long id = jwtService.getUserIdByEncodedToken(jwt);
        userService.update(id, userUpdatingRequest);
        return ResponseEntity.ok("Successful");
    }

    @PostMapping(value = "/update" , consumes = "multipart/form-data")
    public ResponseEntity<String> updateUser ()

}
