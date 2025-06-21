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

    @PostMapping("/{userId}")
    public AuthResponse createAccount(@PathVariable String userId, @RequestBody AccountDetails accountDetails) {
        accountDetailsService.saveAccountDetails(userId,accountDetails);
        return new AuthResponse(HttpStatus.CREATED.value(), "ok",null);
    }

    @GetMapping("/user/{userId}")
    public AuthResponse getAccountDetailsByUserId(@PathVariable String userId){
        Optional<AccountDetails> accountDetailsList = accountDetailsService.getUserAccountDetails(userId);
        return new AuthResponse(HttpStatus.OK.value(), "success", accountDetailsList);
    }

    @GetMapping
    public ResponseEntity<List<AccountDetails>> getAllAccounts() {
        return ResponseEntity.ok(accountDetailsService.getAllAccountDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDetails> getAccountById(@PathVariable Long id) {
        return accountDetailsService.getAccountDetailsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/account/{id}")
    public AuthResponse updateAccountDetails(
            @PathVariable Long id,
            @RequestBody AccountDetails updatedDetails) {
        AccountDetails updated = accountDetailsService.updateAccountDetails(id, updatedDetails);
        return new AuthResponse(HttpStatus.OK.value(), "success",null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        accountDetailsService.deleteAccountDetails(id);
        return ResponseEntity.ok("Account deleted successfully");
    }
}
