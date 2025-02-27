package com.kaan.Blog.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record RegisterRequest (@NotNull String name , @NotNull String lastname , @NotNull String username ,
                               @NotNull String password , @NotNull LocalDate birthDate , MultipartFile image ,
                               @Email String email) {
}
