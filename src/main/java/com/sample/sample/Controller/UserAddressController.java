package com.sample.sample.Controller;



import com.sample.sample.Model.UserAddress;
import com.sample.sample.Service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-address")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @PostMapping
    public ResponseEntity<UserAddress> createAddress(@RequestBody UserAddress userAddress) {
        UserAddress savedAddress = userAddressService.saveAddress(userAddress);
        return ResponseEntity.ok(savedAddress);
    }

    @GetMapping
    public ResponseEntity<List<UserAddress>> getAllAddresses() {
        return ResponseEntity.ok(userAddressService.getAllAddresses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAddress> getAddressById(@PathVariable Long id) {
        Optional<UserAddress> address = userAddressService.getAddressById(id);
        return address.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAddress> updateAddress(@PathVariable Long id, @RequestBody UserAddress userAddress) {
        UserAddress updated = userAddressService.updateAddress(id, userAddress);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        userAddressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}

