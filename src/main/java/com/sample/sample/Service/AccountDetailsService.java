package com.sample.sample.Service;


import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Model.User;
import com.sample.sample.Repository.AccountDetailsRepository;
import com.sample.sample.Repository.UserRepo;
import com.sample.sample.Responses.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AccountDetailsService {

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private UserRepo userRepo;

    public AuthResponse saveAccountDetails(String userId, AccountDetails accountDetails) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with ID: " + userId));

        accountDetails.setUser(user);
        accountDetails.setAccountEmail(user.getEmail());
        user.setAccountDetails(accountDetails);
        accountDetailsRepository.save(accountDetails);

        return new AuthResponse(HttpStatus.CREATED.value(), "created", null);
    }


    public AuthResponse getAllAccountDetails() {
        List<AccountDetails> accounts = accountDetailsRepository.findAll();
        return new AuthResponse(HttpStatus.OK.value(), "ok", accounts);
    }


    public AuthResponse getUserAccountDetails(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with ID: " + userId));

        Optional<AccountDetails> accountDetails = accountDetailsRepository.findByUserId(userId);

        if (accountDetails.isPresent()) {
            return new AuthResponse(HttpStatus.OK.value(), "fetched", accountDetails.get());
        } else {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Account details not found for user ID: " + userId, null);
        }
    }

    public AuthResponse updateAccountDetails(Long id, AccountDetails updatedDetails) {
        AccountDetails existingDetails = accountDetailsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "AccountDetails not found with ID: " + id));


        existingDetails.setFirstName(updatedDetails.getFirstName());
        existingDetails.setLastName(updatedDetails.getLastName());
        existingDetails.setPhone(updatedDetails.getPhone());
        existingDetails.setAlternatePhone(updatedDetails.getAlternatePhone());

        accountDetailsRepository.save(existingDetails);

        return new AuthResponse(HttpStatus.OK.value(), "ok", null);
    }



    public AuthResponse getAccountDetailsById(Long id) {
        Optional<AccountDetails> accountDetails = accountDetailsRepository.findById(id);

        if (accountDetails.isPresent()) {
            return new AuthResponse(HttpStatus.OK.value(), "fetched", accountDetails.get());
        } else {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "AccountDetails not found with ID: " + id, null);
        }
    }


    public AuthResponse deleteAccountDetails(Long id) {
        if (!accountDetailsRepository.existsById(id)) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "AccountDetails not found with ID: " + id, null);
        }

        accountDetailsRepository.deleteById(id);
        return new AuthResponse(HttpStatus.OK.value(), "ok", null);
    }

}
