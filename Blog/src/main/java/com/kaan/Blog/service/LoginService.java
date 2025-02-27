package com.kaan.Blog.service;

import com.kaan.Blog.dto.request.LoginRequest;
import com.kaan.Blog.model.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private JwtService jwtService ;

    private AuthenticationManager authenticationManager ;

    private UserService userService ;

    public LoginService(JwtService jwtService , AuthenticationManager authenticationManager , UserService userService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager ;
        this.userService = userService ;
    }

    public void login (LoginRequest loginRequest , HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password())) ;
        if (authentication != null) {
            Token token = jwtService.getByUserId(userService.getUserIdByUsername(loginRequest.username())) ;
            String jwt = null;
            if (token == null) {
                jwt = jwtService.createJwt(userService.getUserIdByUsername(loginRequest.username()) , loginRequest.username()) ;
            }
            else jwt = token.getJwt();
            if (token != null && !token.isEnabled()) {
                jwt = jwtService.refresh(jwt) ;
            }
            Cookie cookie = new Cookie("Authorization" , "Bearer+"+jwt) ;
            response.addCookie(cookie);
        }
    }



}
