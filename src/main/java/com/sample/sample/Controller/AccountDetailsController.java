package com.sample.sample.Controller;


import com.sample.sample.DTO.AccountDetailsDTO;
import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
@CrossOrigin("*")
public class AccountDetailsController {

    @Autowired
    private AccountDetailsService accountDetailsService;

//    @PostMapping("/{userId}")
//    public AuthResponse createAccount(@PathVariable String userId, @RequestBody AccountDetails accountDetails) {
//        accountDetailsService.saveAccountDetails(userId,accountDetails);
//        return new AuthResponse(HttpStatus.CREATED.value(), "ok",null);
//    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<AuthResponse> getAccountDetailsByUserId(@PathVariable String userId) {
        AuthResponse response = accountDetailsService.getUserAccountDetails(userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/all")
    public ResponseEntity<AuthResponse> getAllAccountDetails() {
        AuthResponse response = accountDetailsService.getAllAccountDetails();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }


    @GetMapping
    public ResponseEntity<AuthResponse> getAllAccounts() {
        AuthResponse response = accountDetailsService.getAllAccountDetails();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthResponse> getAccountById(@PathVariable Long id) {
        AuthResponse response = accountDetailsService.getAccountDetailsById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PutMapping("/account/{id}")
    public ResponseEntity<?> updateAccountDetails(
            @PathVariable Long id,
            @RequestBody AccountDetails updatedDetails) {
        AuthResponse response = accountDetailsService.updateAccountDetails(id, updatedDetails);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        AuthResponse response = accountDetailsService.deleteAccountDetails(id);
        return  ResponseEntity.status(response.getCode()).body(response);
    }

}
