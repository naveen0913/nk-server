package com.sample.sample.Service;

import com.sample.sample.DTO.LoginDTO;
import com.sample.sample.DTO.SignupDTO;
import com.sample.sample.JWT.JwtUtil;
import com.sample.sample.Model.User;
import com.sample.sample.Repository.UserRepo;
import com.sample.sample.Responses.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService{
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse registerUser(SignupDTO signUpDTO, boolean isAdmin) {
        Optional<User> existedUser = userRepository.findByEmail(signUpDTO.getEmail());
        if (existedUser.isPresent()){
            new AuthResponse(HttpStatus.CONFLICT.value(), "User already exists", null);
        }
        User newUser = new User();
        newUser.setUsername(signUpDTO.getUsername());
        newUser.setEmail(signUpDTO.getEmail());
        newUser.setPassword(signUpDTO.getPassword());
        newUser.setCreated(new Date());
        String userRole = "user";
        newUser.setRole(userRole);
        userRepository.save(newUser);
        return new AuthResponse(HttpStatus.CREATED.value(), "created",null);
    }

    public AuthResponse adminRegister(SignupDTO signUpDTO,boolean isAdmin) {
        Optional<User> existedUser = userRepository.findByEmail(signUpDTO.getEmail());
        if (existedUser.isPresent()){
            new AuthResponse(HttpStatus.CONFLICT.value(), "User already exists", null);
        }
        User newUser = new User();
        newUser.setUsername(signUpDTO.getUsername());
        newUser.setEmail(signUpDTO.getEmail());
        newUser.setPassword(signUpDTO.getPassword());
        newUser.setCreated(new Date());
        String userRole = "admin";
        newUser.setRole(userRole);
        userRepository.save(newUser);
        return new AuthResponse(HttpStatus.CREATED.value(), "created",null);
    }

    public AuthResponse userLogin(LoginDTO loginDTO) {
        Optional<User> existedUser = userRepository.findByEmail(loginDTO.getEmail());

        if (existedUser.isEmpty()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "User not found", null);
        }

        User user = existedUser.get();

        if (!loginDTO.getPassword().equals(user.getPassword())) {
            return new AuthResponse(HttpStatus.BAD_REQUEST.value(), "Incorrect password! Try again", null);
        }

        String token = jwtUtil.generateToken(user.getEmail());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("user", user);
        responseBody.put("token", token);

        return new AuthResponse(HttpStatus.OK.value(), "Login success", responseBody);
    }

    public AuthResponse getUserById(String userId){
        Optional<User> existedUser = userRepository.findById(userId);
        if (existedUser.isEmpty()){
            new AuthResponse(HttpStatus.NOT_FOUND.value(), "User not found", null);
        }
        return new AuthResponse(HttpStatus.OK.value(), "login success",existedUser);
    }

    public AuthResponse updateUserDetails(String userId,SignupDTO signUpDTO){
        Optional<User> existedUser = userRepository.findById(userId);
        if (existedUser.isEmpty()){
            new AuthResponse(HttpStatus.NOT_FOUND.value(), "User not found", null);
        }
        User user = existedUser.get();
        if (signUpDTO.getUsername() != null) {
            user.setUsername(signUpDTO.getUsername());
        }
        if (signUpDTO.getEmail() != null) {
            user.setEmail(signUpDTO.getEmail());
        }
        if (signUpDTO.getPassword() != null) {
            user.setPassword(signUpDTO.getPassword()); // Consider hashing passwords before saving!
        }
        userRepository.save(user);
        return new AuthResponse(HttpStatus.OK.value(), "User updated successfully", user);

    }

    public AuthResponse getAllUsers() {
        List<User> users = userRepository.findAll();
        return new AuthResponse(HttpStatus.OK.value(), "users list", users);
    }


    public long getUserCount() {
        return userRepository.count();
    }
}