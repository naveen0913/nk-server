package com.sample.sample.Controller;


import com.sample.sample.Model.UserAddress;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-address")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @PostMapping("/{accountId}")
    public ResponseEntity<?> createAddress(@PathVariable Long accountId, @RequestBody UserAddress userAddress) {
        AuthResponse authResponse = userAddressService.saveAddress(accountId,userAddress);
        return ResponseEntity.status(authResponse.getCode()).body(authResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        AuthResponse authResponse =  userAddressService.deleteAddress(id);
        return ResponseEntity.status(authResponse.getCode()).body(authResponse);
    }


    @GetMapping("/{accountId}/all")
    public ResponseEntity<AuthResponse> getAllUserAddresses(@PathVariable Long accountId) {
        return ResponseEntity.ok(userAddressService.getAllUserAddress(accountId));
    }


    @GetMapping
    public ResponseEntity<AuthResponse> getAllAddresses() {
        return ResponseEntity.ok(userAddressService.getAllAddresses());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable Long id) {
        AuthResponse authResponse = userAddressService.getAddressById(id);
        return ResponseEntity.status(authResponse.getCode()).body(authResponse);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse> updateAddress(@PathVariable Long id, @RequestBody UserAddress userAddress) {
        return ResponseEntity.ok(userAddressService.updateAddress(id, userAddress));
    }

}

