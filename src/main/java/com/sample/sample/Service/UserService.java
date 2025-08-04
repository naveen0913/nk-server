package com.sample.sample.Service;


import com.sample.sample.DTO.ChangePasswordRequest;
import com.sample.sample.DTO.LoginDTO;
import com.sample.sample.DTO.SignupDTO;
import com.sample.sample.JWT.JwtUtil;
import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Model.User;
import com.sample.sample.Repository.AccountDetailsRepository;
import com.sample.sample.Repository.UserRepo;
import com.sample.sample.Responses.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MailService mailService;

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    public AuthResponse registerUser(SignupDTO signUpDTO, boolean isAdmin) {
        Optional<User> existedUser = userRepository.findByEmail(signUpDTO.getEmail());
        if (existedUser.isPresent()) {
            return new AuthResponse(HttpStatus.CONFLICT.value(), "User already exists", null);
        }

        User newUser = new User();
        newUser.setUsername(signUpDTO.getUsername());
        newUser.setEmail(signUpDTO.getEmail());
        newUser.setPassword(signUpDTO.getPassword());
        newUser.setCreated(new Date());
        newUser.setRole("user");
        newUser.setPasswordUpdated(false);

        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setAccountEmail(signUpDTO.getEmail());
        accountDetails.setUser(newUser);

        newUser.setAccountDetails(accountDetails);
        userRepository.save(newUser);
        accountDetailsRepository.save(accountDetails);

        return new AuthResponse(HttpStatus.CREATED.value(), "created", null);
    }

    public AuthResponse adminRegister(SignupDTO signUpDTO, boolean isAdmin) {
        Optional<User> existedUser = userRepository.findByEmail(signUpDTO.getEmail());
        if (existedUser.isPresent()) {
            return new AuthResponse(HttpStatus.CONFLICT.value(), "User already exists", null);
        }

        User newUser = new User();
        newUser.setUsername(signUpDTO.getUsername());
        newUser.setEmail(signUpDTO.getEmail());
        newUser.setPassword(signUpDTO.getPassword());
        newUser.setCreated(new Date());
        newUser.setRole("admin");
        newUser.setPasswordUpdated(false);
        userRepository.save(newUser);

        return new AuthResponse(HttpStatus.CREATED.value(), "created", null);
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

    public AuthResponse getUserById(String userId) {
        Optional<User> existedUser = userRepository.findById(userId);
        if (existedUser.isEmpty()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "User not found", null);
        }

        return new AuthResponse(HttpStatus.OK.value(), "login success", existedUser);
    }

    public AuthResponse updateUserDetails(String userId, SignupDTO signUpDTO) {
        Optional<User> existedUser = userRepository.findById(userId);
        if (existedUser.isEmpty()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "User not found", null);
        }

        User user = existedUser.get();

        if (signUpDTO.getUsername() != null) {
            user.setUsername(signUpDTO.getUsername());
        }
        if (signUpDTO.getEmail() != null) {
            user.setEmail(signUpDTO.getEmail());
        }
        if (signUpDTO.getPassword() != null) {
            user.setPassword(signUpDTO.getPassword()); // Consider hashing!
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

    public void sendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with email: " + email));

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setResetOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());

        userRepository.save(user);
        mailService.sendOtpEmail(user.getEmail(), otp, user.getUsername());
    }

    public void resetPasswordWithOtp(String otp, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        User user = userRepository.findByResetOtp(otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (user.getOtpGeneratedTime() == null || user.getOtpGeneratedTime().isBefore(LocalDateTime.now().minusMinutes(10))) {
            throw new RuntimeException("OTP expired");
        }

        user.setPassword(newPassword);
        user.setPasswordUpdated(true);
        user.setPasswordUpdatedTime(LocalDateTime.now());
        userRepository.save(user);

        mailService.sendResetSuccessMail(user.getEmail(), user.getUsername());
    }

    // ✅ NEW: Change Password
    public void changePassword(String userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!request.getCurrentPassword().equals(user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new RuntimeException("New passwords do not match");
        }

        user.setPassword(request.getNewPassword());
        user.setPasswordUpdated(true);
        user.setPasswordUpdatedTime(LocalDateTime.now());

        userRepository.save(user);
//        mailService.sendResetSuccessMail(user.getEmail(), user.getUsername());
    }

}
