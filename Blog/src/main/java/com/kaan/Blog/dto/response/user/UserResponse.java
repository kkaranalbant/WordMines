package com.kaan.Blog.dto.response.user;

import java.time.LocalDate;

public record UserResponse(String username, String name, String lastname, LocalDate birthDate , String email , byte[] image) {
}
