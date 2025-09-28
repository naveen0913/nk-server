package com.sample.sample.Controller;

import com.sample.sample.DTO.*;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public AuthResponse createUser(@RequestBody SignupDTO signUpDTO) {
        return userService.registerUser(signUpDTO, false);
    }

    @PostMapping("/admin")
    public AuthResponse createAdmin(@RequestBody SignupDTO signUpDTO) {
        return userService.adminRegister(signUpDTO, true);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> userLogin(@RequestBody LoginDTO loginDTO) {
        AuthResponse response = userService.userLogin(loginDTO);
        if (response.getCode() != HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> responseData = (Map<String, Object>) response.getData();
        String token = (String) responseData.get("token");

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false) // true in production with HTTPS
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);

//        return userService.userLogin(loginDTO);
    }


    @GetMapping("/authorized")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        AuthResponse response = userService.getUserFromRequest(request);

        if (response.getCode() != HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public AuthResponse getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("update/{userId}")
    public AuthResponse updateUser(@PathVariable String userId, @RequestBody SignupDTO signUpDTO) {
        return userService.updateUserDetails(userId, signUpDTO);
    }

    @GetMapping("/all")
    public AuthResponse getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        long count = userService.getUserCount();
        return ResponseEntity.ok(count);
    }

    @PostMapping("/forgot-password")
    public AuthResponse sendOtp(@RequestBody ForgotPasswordDTO request) {
        userService.sendOtp(request.email);
        return new AuthResponse(HttpStatus.OK.value(), "OTP sent Succesful", null);
    }

    @PostMapping("/reset-password")
    public AuthResponse resetPassword(@RequestBody ResetDTO request) {
        userService.resetPasswordWithOtp(request.otp, request.newPassword, request.confirmPassword);
        return new AuthResponse(HttpStatus.OK.value(), "password reset successful", null);
    }

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable String userId, @RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(userId, request);
            return ResponseEntity.ok(new AuthResponse(
                    HttpStatus.OK.value(),
                    "Password changed successfully",
                    null
            ));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            "Password change failed " + e.getMessage(),
                            null
                    ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false) // set true in production
                .path("/")
                .maxAge(0) // delete cookie immediately
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Logged out successfully"));
    }


}

