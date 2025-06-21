package com.sample.sample.Repository;

import com.sample.sample.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,String> {


    Optional<User> findByEmail(String email);
    long count();
    long countByRole(String role);

    Optional<User> findByResetOtp(String otp);
    Optional<User> findByUsername(String username);



}
