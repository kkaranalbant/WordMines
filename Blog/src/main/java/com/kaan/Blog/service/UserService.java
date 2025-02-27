package com.kaan.Blog.service;

import com.kaan.Blog.dto.request.user.UserUpdatingRequest;
import com.kaan.Blog.dto.response.user.UserResponse;
import com.kaan.Blog.exception.UserException;
import com.kaan.Blog.mapper.UserMapper;
import com.kaan.Blog.model.Role;
import com.kaan.Blog.model.User;
import com.kaan.Blog.repo.UserRepo;
import com.kaan.Blog.util.FileUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private static final int MIN_AGE;

    private static final int MIN_NAME_LENGTH;

    private static final int MIN_USERNAME_LENGTH;

    private static final int MIN_PASSWORD_LENGTH;

    static {
        MIN_AGE = 15;
        MIN_NAME_LENGTH = 2;
        MIN_USERNAME_LENGTH = 6;
        MIN_PASSWORD_LENGTH = 8;
    }

    private final UserRepo userRepo;

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passEncoder;

    public UserService(UserRepo userRepo, @Lazy BCryptPasswordEncoder passEncoder) {
        this.userRepo = userRepo;
        userMapper = UserMapper.INSTANCE;
        this.passEncoder = passEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
    }

    public User getUserById(Long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new UserException("User Not Found"));
    }

    public Long getUserIdByUsername(String username) throws UserException {
        User user = userRepo.findUserByUsername(username).orElse(null);
        if (user != null) {
            return user.getId();
        }
        throw new UserException("Username Not Found");
    }

    public UserResponse getUserByIdForFrontend(Long userId) {
        return convert(getUserById(userId));
    }

    private UserResponse convert(User user) {
        UserResponse userResponse = new UserResponse(user.getUsername(), user.getName(), user.getLastname(), user.getBirthDate(), user.getEmail(), user.getImage());
        return userResponse;
    }

    public void update(Long userId, UserUpdatingRequest userUpdatingRequest) throws UserException {
        validateUserUpdatingRequest(userUpdatingRequest);
        User user = userMapper.userUpdatingrDtoToUser(userUpdatingRequest);
        if (isUniqueEmail(user.getEmail())) {
            throw new UserException("This Email is Registered In Our System.");
        }
        if (isUniqueUsername(user.getUsername())) {
            throw new UserException("You Can't Choose This Username");
        }
        if (user.getPassword() == null) {
            User oldUser = userRepo.findById(userId).get();
            user.setPassword(passEncoder.encode(oldUser.getPassword()));
        } else {
            user.setPassword(passEncoder.encode(user.getPassword()));
        }
        user.setId(userId);
        user.setImage(FileUtil.convertMultipartFileToByteArray(userUpdatingRequest.image()));
        user.setRole(Role.USER);
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        userRepo.save(user);
    }

    private void validateUserUpdatingRequest(UserUpdatingRequest request) throws UserException {
        if (request.name().length() < MIN_NAME_LENGTH) throw new UserException("Please Enter Valid Name");
        if (request.lastname().length() < MIN_NAME_LENGTH) throw new UserException("Please Enter Valid Lastname");
        if (request.birthDate().plusYears(MIN_AGE).isAfter(LocalDate.now()))
            throw new UserException("Your Age Must Be At Least " + MIN_AGE);
        if (request.username().length() < MIN_USERNAME_LENGTH)
            throw new UserException("Username Must Be At Least " + MIN_USERNAME_LENGTH + " Characters");
        if (request.password() != null && request.password().length() < MIN_PASSWORD_LENGTH)
            throw new UserException("Password Must Be At Least " + MIN_PASSWORD_LENGTH + " Characters");
    }

    private boolean isUniqueEmail(String email) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        return userOptional.isEmpty();
    }

    private boolean isUniqueUsername(String username) {
        Optional<User> userOptional = userRepo.findUserByUsername(username);
        return userOptional.isEmpty();
    }
}
