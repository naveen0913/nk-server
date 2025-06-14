package com.sample.sample.Controller;


import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Service.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountDetailsController {

    @Autowired
    private AccountDetailsService accountDetailsService;

    @PostMapping
    public ResponseEntity<AccountDetails> createAccount(@RequestBody AccountDetails accountDetails) {
        AccountDetails savedAccount = accountDetailsService.saveAccountDetails(accountDetails);
        return ResponseEntity.ok(savedAccount);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        accountDetailsService.deleteAccountDetails(id);
        return ResponseEntity.ok("Account deleted successfully");
    }
}
