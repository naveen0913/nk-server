package com.sample.sample.Service;


import com.sample.sample.DTO.AccountDetailsDTO;
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

//    public AuthResponse saveAccountDetails(String userId, AccountDetails accountDetails) {
//
//        User user = userRepo.findById(userId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "User not found with ID: " + userId));
//        accountDetails.setUser(user);
//        accountDetails.setAccountEmail(user.getEmail());
//        user.setAccountDetails(accountDetails);
//        accountDetailsRepository.save(accountDetails);
//        return new AuthResponse(HttpStatus.CREATED.value(), "ok",null);
//    }

    public List<AccountDetails> getAllAccountDetails() {
        List<AccountDetails> accounts = accountDetailsRepository.findAll();
        return accounts;
    }

    public Optional<AccountDetails> getUserAccountDetails(String userId){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with ID: " + userId));
        return accountDetailsRepository.findByUserId(userId);
    }

    public AccountDetails updateAccountDetails(Long id, AccountDetails updatedDetails) {
        AccountDetails existingDetails = accountDetailsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "AccountDetails not found with ID: " + id));

        // Update the fields you want to allow changes for
        existingDetails.setFirstName(updatedDetails.getFirstName());
        existingDetails.setLastName(updatedDetails.getLastName());
        existingDetails.setPhone(updatedDetails.getPhone());
        existingDetails.setAlternatePhone(updatedDetails.getAlternatePhone());

        return accountDetailsRepository.save(existingDetails);
    }



    public Optional<AccountDetails> getAccountDetailsById(Long id) {
        return accountDetailsRepository.findById(id);
    }

    public void deleteAccountDetails(Long id) {
        accountDetailsRepository.deleteById(id);
    }
}
