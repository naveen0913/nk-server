package com.sample.sample.Service;


import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Model.UserAddress;
import com.sample.sample.Repository.AccountDetailsRepository;
import com.sample.sample.Repository.UserAddressRepository;
import com.sample.sample.Responses.AuthResponse;
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

    public AuthResponse saveAddress(Long accountId, UserAddress userAddress) {
        AccountDetails accountDetails = accountDetailsRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account not found with ID: " + accountId));

        userAddress.setAccountDetails(accountDetails);
        UserAddress savedAddress = userAddressRepository.save(userAddress);

        return new AuthResponse(HttpStatus.CREATED.value(), "Address saved successfully", null);
    }


    public AuthResponse getAllUserAddress(Long accountId) {
        List<UserAddress> addresses = userAddressRepository.getAllUserAddress(accountId);
        return new AuthResponse(HttpStatus.OK.value(), "User addresses fetched successfully", addresses);
    }

    public AuthResponse getAllAddresses() {
        List<UserAddress> allAddresses = userAddressRepository.findAll();
        return new AuthResponse(HttpStatus.OK.value(), "All addresses fetched successfully", allAddresses);
    }

    public AuthResponse getAddressById(Long id) {
        Optional<UserAddress> optionalAddress = userAddressRepository.findById(id);

        if (optionalAddress.isPresent()) {
            return new AuthResponse(HttpStatus.OK.value(), "Address found", optionalAddress.get());
        } else {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Address not found with ID: " + id, null);
        }
    }

    public AuthResponse deleteAddress(Long id) {
        boolean exists = userAddressRepository.existsById(id);
        if (!exists) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Address not found with ID: " + id, null);
        }

        userAddressRepository.deleteById(id);
        return new AuthResponse(HttpStatus.OK.value(), "Address deleted successfully", null);
    }


    public AuthResponse updateAddress(Long id, UserAddress updatedAddress) {
        Optional<UserAddress> optionalAddress = userAddressRepository.findById(id);

        if (optionalAddress.isPresent()) {
            UserAddress existingAddress = optionalAddress.get();

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

            UserAddress savedAddress = userAddressRepository.save(existingAddress);
            return new AuthResponse(HttpStatus.OK.value(), "Address updated successfully", savedAddress);
        } else {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Address not found with ID: " + id, null);
        }
    }

}

