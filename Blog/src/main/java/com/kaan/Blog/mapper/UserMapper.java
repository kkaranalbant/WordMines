package com.kaan.Blog.mapper;

import com.kaan.Blog.dto.request.user.RegisterRequest;
import com.kaan.Blog.dto.request.user.UserUpdatingRequest;
import com.kaan.Blog.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "image", target = "image", ignore = true)
    User userUpdatingrDtoToUser(UserUpdatingRequest userUpdatingRequest);

    @Mapping(source = "image", target = "image", ignore = true)
    UserUpdatingRequest userToUserUpdatingRequest(User user);

    @Mapping(source = "image" , target = "image" , ignore = true)
    RegisterRequest userToRegisterRequest (User user) ;

    @Mapping(source = "image" , target = "image" , ignore = true)
    User registerRequestToUser (RegisterRequest registerRequest) ;

}
