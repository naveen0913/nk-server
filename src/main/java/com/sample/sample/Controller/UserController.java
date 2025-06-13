package com.sample.sample.Controller;


import com.sample.sample.DTO.ForgotPasswordDTO;
import com.sample.sample.DTO.LoginDTO;
import com.sample.sample.DTO.ResetDTO;
import com.sample.sample.DTO.SignupDTO;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/signup")
    public AuthResponse createUser(@RequestBody SignupDTO signUpDTO){
        return userService.registerUser(signUpDTO,false);
    }


    @PostMapping("/admin")
    public AuthResponse createAdmin(@RequestBody SignupDTO signUpDTO){
        return userService.adminRegister(signUpDTO,true);
    }

    @PostMapping("/login")
    public AuthResponse userLogin(@RequestBody LoginDTO loginDTO){
        return userService.userLogin(loginDTO);
    }

    @GetMapping("/{userId}")
    public AuthResponse getUserById(@PathVariable String userId){
        return userService.getUserById(userId);
    }

    @PutMapping("update/{userId}")
    public AuthResponse updateUser(@PathVariable String userId,@RequestBody SignupDTO signUpDTO){
        return userService.updateUserDetails(userId,signUpDTO);
    }

    @GetMapping("/all")
    public AuthResponse getAllUsers(){
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
        return new AuthResponse(HttpStatus.OK.value(), "OTP sent Succesful",null);
    }

    @PostMapping("/reset-password")
    public AuthResponse resetPassword(@RequestBody ResetDTO request) {

        userService.resetPasswordWithOtp(request.otp, request.newPassword, request.confirmPassword);
        return new AuthResponse(HttpStatus.OK.value(), "password reset successful",null);
    }
}