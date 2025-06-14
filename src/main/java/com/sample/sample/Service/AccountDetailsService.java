package com.sample.sample.Service;


import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Repository.AccountDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountDetailsService {

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    public AccountDetails saveAccountDetails(AccountDetails accountDetails) {
        return accountDetailsRepository.save(accountDetails);
    }

    public List<AccountDetails> getAllAccountDetails() {
        return accountDetailsRepository.findAll();
    }

    public Optional<AccountDetails> getAccountDetailsById(Long id) {
        return accountDetailsRepository.findById(id);
    }

    public void deleteAccountDetails(Long id) {
        accountDetailsRepository.deleteById(id);
    }
}
