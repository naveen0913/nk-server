package com.sample.sample.Service;

import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Repository.AccountDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountDetailsService {

    @Autowired
    private AccountDetailsRepository repository;

    public AccountDetails saveAccount(AccountDetails accountDetails) {
        return repository.save(accountDetails);
    }

    public AccountDetails updateAccount(Long id, AccountDetails updatedDetails) {
        Optional<AccountDetails> optional = repository.findById(id);
        if (optional.isPresent()) {
            AccountDetails existing = optional.get();
            existing.setFirstName(updatedDetails.getFirstName());
            existing.setLastName(updatedDetails.getLastName());
            existing.setEmail(updatedDetails.getEmail());
            existing.setPhone(updatedDetails.getPhone());
            existing.setAddress(updatedDetails.getAddress());
            return repository.save(existing);
        } else {
            throw new RuntimeException("Account not found with ID: " + id);
        }
    }

    public AccountDetails getAccount(Long id) {
        return repository.findById(id).orElse(null);
    }
}
