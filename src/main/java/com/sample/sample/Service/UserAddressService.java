package com.sample.sample.Service;


import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Model.UserAddress;
import com.sample.sample.Repository.AccountDetailsRepository;
import com.sample.sample.Repository.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserAddressService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    public UserAddress saveAddress(Long accountId,UserAddress userAddress) {
        AccountDetails accountDetails = accountDetailsRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account not found with ID: " + accountId));

        userAddress.setAccountDetails(accountDetails);
        return userAddressRepository.save(userAddress);
    }

    public List<UserAddress> getAllUserAddress(Long accountId){
        return userAddressRepository.getAllUserAddress(accountId);
    }

    public List<UserAddress> getAllAddresses() {
        return userAddressRepository.findAll();
    }

    public Optional<UserAddress> getAddressById(Long id) {
        return userAddressRepository.findById(id);
    }

    public void deleteAddress(Long id) {
        userAddressRepository.deleteById(id);
    }

    public UserAddress updateAddress(Long id, UserAddress updatedAddress) {
        return userAddressRepository.findById(id).map(existingAddress -> {
            existingAddress.setFirstName(updatedAddress.getFirstName());
            existingAddress.setLastName(updatedAddress.getLastName());
            existingAddress.setPhone(updatedAddress.getPhone());
            existingAddress.setAlterPhone(updatedAddress.getAlterPhone());
            existingAddress.setAddressType(updatedAddress.getAddressType());
            existingAddress.setAddressLine1(updatedAddress.getAddressLine1());
            existingAddress.setAddressLine2(updatedAddress.getAddressLine2());
            existingAddress.setCity(updatedAddress.getCity());
            existingAddress.setState(updatedAddress.getState());
            existingAddress.setCountry(updatedAddress.getCountry());
            existingAddress.setPincode(updatedAddress.getPincode());
            return userAddressRepository.save(existingAddress);
        }).orElse(null);
    }
}

