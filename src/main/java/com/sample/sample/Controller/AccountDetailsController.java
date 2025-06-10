package com.sample.sample.Controller;

import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Service.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@CrossOrigin("*")
public class AccountDetailsController {

    @Autowired
    private AccountDetailsService service;

    // Create Account
    @PostMapping
    public ResponseEntity<AccountDetails> saveAccount(@RequestBody AccountDetails accountDetails) {
        return ResponseEntity.ok(service.saveAccount(accountDetails));
    }

    // Update Account
    @PutMapping("/{id}")
    public ResponseEntity<AccountDetails> updateAccount(@PathVariable Long id, @RequestBody AccountDetails accountDetails) {
        return ResponseEntity.ok(service.updateAccount(id, accountDetails));
    }

    // Get Account by ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountDetails> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAccount(id));
    }
}

